package com.maxplus1.ms_world.fallback.factory;

import com.maxplus1.ms_world.fallback.HelloFallBack;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * Created by xiaolong.qiu on 2017/5/12.
 */
@Component
public class HelloFallBackFactory implements FallbackFactory<HelloFallBack> {
    @Override
    public HelloFallBack create(Throwable throwable) {

        return new HelloFallBack() {
            @Override
            public String failBack() {
                System.out.println("trigger fall back……");
                return "Hello Error,fail back method runs!";
            }
        };
    }
}
