# spring-cloud-demo
A Demo Of Spring Cloud

# 步骤
- 导入IDE
- 执行父项目Spring-cloud-demo的maven命令，分别是clean和package
- 之后执行命令：
```bash
# 其实在win下命令不好使，还是一条条执行吧……
cd D:\xxx\xxx\spring-cloud-demo #项目所在目录
java -jar 0-ms-config-center/target/ms-config-center-1.0-SNAPSHOT.jar
java -jar 1-ms-admin/target/ms-admin-1.0-SNAPSHOT.jar
java -jar 2-ms-registry-discovery/target/ms-registry-discovery-1.0-SNAPSHOT.jar
java -jar 3-ms-gateway/target/ms-gateway-1.0-SNAPSHOT.jar
java -jar 4-ms-hello/target/ms-hello-1.0-SNAPSHOT.jar
java -jar 5-ms-world/target/ms-world-1.0-SNAPSHOT.jar
```
# 访问地址
- Spring Boot Admin
http://localhost:20001/#/
- Eureka
http://localhost:20002/#/
- 直接访问服务
http://localhost:20004/hello
http://localhost:20005/world
- 代理访问
http://localhost:20003/ms-hello/hello
http://localhost:20003/ms-world/world

# 配置之坑
如果是下划线ms-hello ,则`host = new URI(url).getHost();`会返回null，
从而导致`org.springframework.cloud.netflix.feign.FeignClientsRegistrar#validate` 抛出校验异常 FeignClients不可用


# PS
- 网关用于控制对外提供的路由，或者用于界面应用对服务端的请求路由。可用nginx替代（服务端负载均衡）
- 内部服务治理则直接基于Eureka实现服务注册发现，Ribbon（Feign）实现负载均衡（客户端负载均衡）
