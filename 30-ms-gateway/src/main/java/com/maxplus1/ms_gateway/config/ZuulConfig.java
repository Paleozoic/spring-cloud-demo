package com.maxplus1.ms_gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 这里让我想起我另一个开源项目blog_admin动态更新Shiro权限的处理。目的都是刷新内存的数据。
 * 需要实现目标：
 * （1）支持加载配置文件设置的路由
 * （2）支持加载数据库的配置的路由
 * （3）支持实时更新的路由
 * 相关文章：
 * [zuul源码分析之Request生命周期管理](http://blog.csdn.net/haha7289/article/details/54312043)
 * [springcloud----Zuul动态路由](http://blog.csdn.net/u013815546/article/details/68944039)
 * [zuul源码分析--Filter注册](https://my.oschina.net/u/3300636/blog/851984?nocache=1491877235421)
 * Created by xiaolong.qiu on 2017/5/19.
 */
@Configuration
public class ZuulConfig {

    @Autowired
    private ServerProperties serverProperties;
    /**
     * 此对象接受@ConfigurationProperties("zuul")的配置属性注入，用来配置zuul路由信息
     */
    @Autowired
    private ZuulProperties zuulProperties;

    @Bean
    public CustomRouteLocator routeLocator() {
        CustomRouteLocator routeLocator = new CustomRouteLocator(this.serverProperties.getServletPrefix(), this.zuulProperties);
        return routeLocator;
    }
}
