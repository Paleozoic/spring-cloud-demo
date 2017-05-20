package com.maxplus1.ms_gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.RoutesRefreshedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xiaolong.qiu on 2017/5/19.
 */
@RestController
public class RefreshRouteService {
    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private CustomRouteLocator routeLocator;

    /**
     * 调用此方法，会刷新路由（更新内存数据）
     */
    @PostMapping("refreshRoute")
    public void refreshRoute() {
        RoutesRefreshedEvent routesRefreshedEvent = new RoutesRefreshedEvent(routeLocator);
        publisher.publishEvent(routesRefreshedEvent);
    }
}
