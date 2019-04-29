package com.vic.staticcontext;

import org.springframework.stereotype.Component;


@Component
public class Worker {

    void doWork(String arg){
        System.out.println("working with: "+ arg);
    }
}
