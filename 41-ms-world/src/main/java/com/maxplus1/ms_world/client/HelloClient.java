package com.maxplus1.ms_world.client;

import com.maxplus1.ms_world.fallback.factory.HelloFallBackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(name = "ms-hello", fallbackFactory = HelloFallBackFactory.class)
public interface HelloClient {

    @GetMapping("hello")
    String hello();

    @GetMapping("hello/error")
    String helloError();

}
