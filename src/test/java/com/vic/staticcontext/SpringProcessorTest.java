package com.vic.staticcontext;

import org.junit.Test;

public class SpringProcessorTest {

    @Test
    public void shouldInitAnThenWork(){
        SpringProcessor processor = new SpringProcessor();
        processor.init();

        processor.process("a");

        processor.process("b");
    }
}
