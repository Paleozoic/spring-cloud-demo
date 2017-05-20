package com.maxplus1.ms_gateway_admin.rest;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaolong.qiu on 2017/5/19.
 */
@RestController
@Data
@Slf4j
@FeignClient
public class RouteAdminRest {

    @Value("${gateway.application.name}")
    private String MS_GATEWAY;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate;


    @PostMapping("refreshRoute")
    public String refreshRoute() {
        List<String> serList = discoveryClient.getServices();
        if (serList.contains(MS_GATEWAY)) {
            List<ServiceInstance> instances = discoveryClient.getInstances(MS_GATEWAY);
            List<String> failInstances = new ArrayList<>();
            instances.forEach(instance -> {
                try {
                    String url = instance.getUri().toURL().toString() ;
                    restTemplate.postForObject(url, null, Void.class, (Object[]) null);
                } catch (Exception e) {
                    try {
                        log.error("[ERROR===>>>]更新路由失败！实例是：" + instance.getUri().toURL().toString() , e);
                        failInstances.add(instance.getUri().toURL().toString());
                    } catch (MalformedURLException e1) {
                        log.error("[ERROR===>>>]更新路由失败！" , e1);
                    }
                }
            });
            return "[ERROR===>>>]更新完毕，更新失败的实例有：" + failInstances.toString();
        } else {
            return "[ERROR===>>>]没有找到服务：" + MS_GATEWAY;
        }
    }
}
