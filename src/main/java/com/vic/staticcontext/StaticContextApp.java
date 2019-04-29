package com.vic.staticcontext;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
public class StaticContextApp {
    private static ApplicationContext context;

    public synchronized static void startSpringBoot() {
        if (context == null) {
            context  = SpringApplication.run(StaticContextApp.class);
        }
    }

    public static ApplicationContext context(){
        if (context == null) {
            throw new IllegalStateException("The context is not initialized");
        }
        return context;
    }
}