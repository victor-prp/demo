package com.vic.async.tasks.demo.service;

import com.vic.async.tasks.demo.dto.OrchReq;
import com.vic.async.tasks.demo.dto.OrchRes;
import com.vic.async.tasks.demo.dto.SourceResponse;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AsyncOrchestratorTest {

    @Test
    public void shouldReturnAllMsgsFromAllSources(){

        List<SourceService> sources = new ArrayList<>();
        SourceService sourceA = mock(SourceService.class);
        when(sourceA.msgs(any())).thenReturn(new SourceResponse("A"));
        sources.add(sourceA);

        SourceService sourceB = mock(SourceService.class);
        when(sourceB.msgs(any())).thenReturn(new SourceResponse("B"));
        sources.add(sourceB);

        Orchestrator orchestrator = new AsyncOrchestrator(sources, 1000);

        OrchRes result = orchestrator.msgs(new OrchReq("test-account"));


        Assert.assertTrue("Must contain msg from sourceA", result.msgs.contains("A"));
        Assert.assertTrue("Must contain msg from sourceB", result.msgs.contains("B"));

    }
}
