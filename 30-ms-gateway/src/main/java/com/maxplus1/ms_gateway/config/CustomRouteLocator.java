package com.maxplus1.ms_gateway.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.cloud.netflix.zuul.filters.ZuulProperties.*;

/**
 * 代码来源：http://blog.csdn.net/u013815546/article/details/68944039
 * Created by xiaolong.qiu on 2017/5/19.
 */
@Slf4j
@Data
public class CustomRouteLocator extends SimpleRouteLocator implements RefreshableRouteLocator {


    private ZuulProperties properties;


    public CustomRouteLocator(String servletPath, ZuulProperties properties) {
        super(servletPath, properties);
        this.properties = properties;
        log.info("servletPath:{}",servletPath);
    }

    //父类已经提供了这个方法，这里写出来只是为了说明这一个方法很重要！！！
//    @Override
//    protected void doRefresh() {
//        super.doRefresh();
//    }


    @Override
    public void refresh() {
        doRefresh();
    }

    @Override
    protected Map<String, ZuulRoute> locateRoutes() {
        LinkedHashMap<String, ZuulRoute> routesMap = new LinkedHashMap<>();
        //从application.properties中加载路由信息
        routesMap.putAll(super.locateRoutes());
        //从db中加载路由信息
        routesMap.putAll(getRoutesFromSomewhere());
        //优化一下配置
        LinkedHashMap<String, ZuulRoute> values = new LinkedHashMap<>();
        for (Map.Entry<String, ZuulRoute> entry : routesMap.entrySet()) {
            String path = entry.getKey();
            // Prepend with slash if not already present.
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (StringUtils.hasText(this.properties.getPrefix())) {
                path = this.properties.getPrefix() + path;
                if (!path.startsWith("/")) {
                    path = "/" + path;
                }
            }
            values.put(path, entry.getValue());
        }
        return values;
    }

    /**
     * 模拟从某处获取路由配置文件
     * @return
     */
    private Map<String, ZuulRoute> getRoutesFromSomewhere(){
        Map<String, ZuulRoute> routes = new LinkedHashMap<>();
        //从其他数据源加载路由信息
        ZuulRoute zuulRoute = new ZuulRoute();
        /**
         * 等价于：
         * zuul:
         *   routes:
         *     ms_world:
         *       path: /world/**
         */
        zuulRoute.setPath("/world/**");
        routes.put("ms-world",zuulRoute);
        return routes;
    }


}
