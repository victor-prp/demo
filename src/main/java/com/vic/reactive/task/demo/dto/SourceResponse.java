package com.vic.reactive.task.demo.dto;

import java.util.Arrays;
import java.util.List;

public class SourceResponse {
    public final List<String> msgs;

    public SourceResponse(String... msgs) {
        this(Arrays.asList(msgs));
    }

    public SourceResponse(List<String> msgs) {
        this.msgs = msgs;
    }
}
