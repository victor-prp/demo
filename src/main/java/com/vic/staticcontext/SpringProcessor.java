package com.vic.staticcontext;

/**
 * Created by victor on 4/29/19.
 */
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
