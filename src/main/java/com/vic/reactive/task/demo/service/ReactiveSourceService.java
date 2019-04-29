package com.vic.reactive.task.demo.service;

import com.vic.async.tasks.demo.dto.SourceRequest;
import com.vic.async.tasks.demo.dto.SourceResponse;
import reactor.core.publisher.Mono;

public interface ReactiveSourceService {
    Mono<SourceResponse> msgs(SourceRequest req);
}
