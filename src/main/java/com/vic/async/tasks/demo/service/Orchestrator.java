package com.vic.async.tasks.demo.service;


import com.vic.async.tasks.demo.dto.OrchReq;
import com.vic.async.tasks.demo.dto.OrchRes;

public interface Orchestrator {
    OrchRes msgs(OrchReq req);
}
