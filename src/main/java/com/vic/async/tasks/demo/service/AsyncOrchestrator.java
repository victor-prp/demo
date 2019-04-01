package com.vic.async.tasks.demo.service;

import com.vic.async.tasks.demo.dto.OrchReq;
import com.vic.async.tasks.demo.dto.OrchRes;
import com.vic.async.tasks.demo.dto.SourceRequest;
import com.vic.async.tasks.demo.dto.SourceResponse;

import java.util.List;
import java.util.concurrent.*;


public class AsyncOrchestrator implements Orchestrator {
    ExecutorService executor = Executors.newFixedThreadPool(10);

    private final List<SourceService> sources;
    private final long timoutMillis;

    public AsyncOrchestrator(List<SourceService> sources, long timoutMillis) {
        this.sources = sources;
        this.timoutMillis = timoutMillis;
    }


    @Override
    public OrchRes msgs(OrchReq req) {

        /**
         * Thread safe list
         */
        List<String> result = new CopyOnWriteArrayList<>();

        /**
         * CountDown in order to wait for completion with timeout
         */
        CountDownLatch countDown = new CountDownLatch(sources.size());

        /**
         * Transform req for sources
         */
        SourceRequest sourceReq =  transform(req);

        /**
         * Invoke all sources asynchronously in parallel and add returned msgs to result
         */
        sources.forEach( source ->
                callSourceAsync(source, sourceReq)
                        .thenAccept(res -> result.addAll(res.msgs))
                        .thenAccept((v)-> countDown.countDown()));

        waitForCompletionWithTimeout(countDown);


        return new OrchRes(result);
    }

    private void waitForCompletionWithTimeout(CountDownLatch countDown) {
        try {
            countDown.await(timoutMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            System.out.println("Interrupted when waiting for sources. Going to take msgs from sources that returned results" + e);
        }
    }

    private static SourceRequest transform(OrchReq req) {
        return new SourceRequest(req.account);
    }


    CompletableFuture<SourceResponse> callSourceAsync(SourceService source, SourceRequest req){
        return CompletableFuture.supplyAsync(()-> callSource(source, req), executor);
    }

    SourceResponse callSource(SourceService source, SourceRequest req){
        return source.msgs(req);
    }
}
