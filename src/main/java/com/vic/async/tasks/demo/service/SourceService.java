package com.vic.async.tasks.demo.service;


import com.vic.async.tasks.demo.dto.SourceRequest;
import com.vic.async.tasks.demo.dto.SourceResponse;

public interface SourceService {
    SourceResponse msgs(SourceRequest req);
}
