package com.vic.reactive.task.demo.service;

import com.vic.async.tasks.demo.dto.OrchReq;
import com.vic.async.tasks.demo.dto.OrchRes;
import reactor.core.publisher.Mono;

public interface ReactiveOrchestrator {
    Mono<OrchRes> msgs(OrchReq req);
}
