package com.vic.reactive.task.demo.service;

import com.vic.async.tasks.demo.dto.OrchReq;
import com.vic.async.tasks.demo.dto.OrchRes;
import com.vic.async.tasks.demo.dto.SourceRequest;
import com.vic.async.tasks.demo.dto.SourceResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.Duration.ofMillis;

public class ReactiveAsyncOrchestrator implements ReactiveOrchestrator {
    private final List<ReactiveSourceService> sources;
    private final long timoutMillis;

    public ReactiveAsyncOrchestrator(List<ReactiveSourceService> sources, long timoutMillis) {
        this.sources = sources;
        this.timoutMillis = timoutMillis;
    }

    @Override
    public Mono<OrchRes> msgs(OrchReq req) {
        /**
         * Transform req for sources
         */
        SourceRequest sourceReq = transform(req);


        Mono<List<SourceResponse>> result = Flux.fromIterable(sources)
                .flatMap(  source -> source.msgs(sourceReq)
                                    .timeout(ofMillis(timoutMillis),
                                             Mono.just(new SourceResponse())))
                .collectList();

        return result.map(this::transform);
    }

    private OrchRes transform(List<SourceResponse> responses) {
        List<String> msgs = responses.stream()
                .flatMap(response -> response.msgs.stream())
                .collect(Collectors.toList());
        return new OrchRes(msgs);
    }

    private static SourceRequest transform(OrchReq req) {
        return new SourceRequest(req.account);
    }

}
