package com.vic.async.tasks.demo.dto;

import java.util.ArrayList;
import java.util.List;

public class OrchRes {
    public final List<String> msgs;

    public OrchRes(List<String> msgs) {
        this.msgs = new ArrayList<>(msgs);
    }
}
