package com.vic.reactive.task.demo.service;

import com.vic.async.tasks.demo.dto.OrchReq;
import com.vic.async.tasks.demo.dto.OrchRes;
import com.vic.async.tasks.demo.dto.SourceResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@Slf4j
public class ReactiveAsyncOrchestratorTest {

    @Test
    public void shouldReturnAllMsgsFromAllSources(){

        List<ReactiveSourceService> sources = new ArrayList<>();

        ReactiveSourceService sourceA = mock(ReactiveSourceService.class);
        when(sourceA.msgs(any())).thenReturn(Mono.just(new SourceResponse("A")));
        sources.add(sourceA);

        ReactiveSourceService sourceB = mock(ReactiveSourceService.class);
        when(sourceB.msgs(any())).thenReturn(Mono.just(new SourceResponse("B")));
        sources.add(sourceB);

        ReactiveOrchestrator orchestrator = new ReactiveAsyncOrchestrator(sources, 1000);

        log.info("Going to call orchestrator");

        Mono<OrchRes> monoResult = orchestrator.msgs(new OrchReq("test-account"));
        log.info("Orchestrator completed");

        OrchRes result = monoResult.block();
        log.info("Result is ready");


        Assert.assertTrue("Must contain msg from sourceA", result.msgs.contains("A"));
        Assert.assertTrue("Must contain msg from sourceB", result.msgs.contains("B"));

    }

    @Test
    public void shouldReturnWithinTimeoutWithPartialResult(){

        List<ReactiveSourceService> sources = new ArrayList<>();

        ReactiveSourceService sourceA = mock(ReactiveSourceService.class);
        when(sourceA.msgs(any())).thenReturn(Mono.just(new SourceResponse("A")).delayElement(Duration.ofMillis(100)));
        sources.add(sourceA);

        ReactiveSourceService sourceB = mock(ReactiveSourceService.class);
        when(sourceB.msgs(any())).thenReturn(Mono.just(new SourceResponse("B")).delayElement(Duration.ofSeconds(5000)));
        sources.add(sourceB);

        ReactiveOrchestrator orchestrator = new ReactiveAsyncOrchestrator(sources, 1000);

        log.info("Going to call orchestrator");

        Mono<OrchRes> monoResult = orchestrator.msgs(new OrchReq("test-account"));
        log.info("Orchestrator completed");

        OrchRes result = monoResult.block();
        log.info("Result is ready");


        Assert.assertTrue("Must contain msg from fast sourceA", result.msgs.contains("A"));
        Assert.assertFalse("Must NOT contain msg from slow sourceB", result.msgs.contains("B"));

    }

    void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted",e);
        }
    }

}
