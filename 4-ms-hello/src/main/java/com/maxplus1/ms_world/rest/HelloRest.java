package com.maxplus1.ms_world.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xiaolong.qiu on 2017/5/12.
 */
@RestController
public class HelloRest {

    @GetMapping("hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("hello/error")
    public String helloError() throws Exception {
        throw new Exception("hello error");
    }
}
