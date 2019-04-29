package com.vic.staticcontext;

public class SpringProcessor implements Processor {
    @Override
    public void init() {
        StaticContextApp.startSpringBoot();
    }

    @Override
    public void process(String arg) {
        Worker worker = StaticContextApp.context().getBean(Worker.class);
        worker.doWork(arg);
    }
}
