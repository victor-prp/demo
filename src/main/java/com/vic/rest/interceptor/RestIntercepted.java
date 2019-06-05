package com.vic.rest.interceptor;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.MappedInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RestController
@RequestMapping("/rest/api/")
public class RestIntercepted {
    private static final String ARG = "arg";
    private ConcurrentHashMap<String, AtomicInteger> counters = new ConcurrentHashMap<>();

    public RestIntercepted() {
        log.info("RestIntercepted is up");
    }

    @PostMapping("/intercepted")
    public InterceptedRes execute(@RequestBody InterceptedReq req,
                                  HttpServletRequest request) throws InterruptedException {

        int counter = getCount(req.getId());
        val result = new InterceptedRes(counter);
        request.setAttribute(ARG, req);
        log.info("result: {}", result);
        Thread.sleep(100);
        return result;
    }

    private int incrementAndGet(String id){
        counters.putIfAbsent(id, new AtomicInteger(0));
        return counters.get(id).incrementAndGet();
    }

    private int getCount(String id){
        counters.putIfAbsent(id, new AtomicInteger(0));
        return counters.get(id).get();
    }


    @Bean
    public MappedInterceptor interceptor() {
        return new MappedInterceptor(new String[]{"/rest/api/intercepted"}, new HandlerInterceptor() {
            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
                InterceptedReq req = (InterceptedReq)request.getAttribute(ARG);
                if (ex != null){
                    log.warn("Failed to complete the request, the: {}",request);
                }else {
                    log.info("The request was successfully processed and the client got the result. New count for id:{} is:{}",req.getId(), incrementAndGet(req.getId()));
                    //Update things after we 100% sure the client got the response.
                }
            }
        });
    }

}
