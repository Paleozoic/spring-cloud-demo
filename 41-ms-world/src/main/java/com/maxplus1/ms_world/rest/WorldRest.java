package com.maxplus1.ms_world.rest;

import com.maxplus1.ms_world.client.HelloClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xiaolong.qiu on 2017/5/12.
 */
@RestController
public class WorldRest {

    @Autowired
    private HelloClient helloClient;

    @GetMapping("world")
    public String world() {
        return helloClient.hello() + " world!";
    }

    @GetMapping("world/error")
    public String worldError() {
        return helloClient.helloError();
    }
}
