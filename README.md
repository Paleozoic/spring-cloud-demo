# spring-cloud-demo
A Demo Of Spring Cloud


# 配置中心
配置中心仓库位于该项目config-repo分支。
`application.yml`则是公共配置文件。
`ms-admin-dev.yml` 表示`spring.application.name`是`ms-admin`的应用，`spring.profiles.active`是`dev`的配置文件。
配置文件位于仓库根目录，每个应用的配置通过文件名来区分。也可以新建仓库或者分支实现区分。

# 步骤
- 导入IDE
- 执行父项目Spring-cloud-demo的maven命令，分别是clean和package
- 之后执行命令：（务必顺序执行）
```bash
# 其实在win下命令不好使，就不写win下批处理脚本了。还是一条条执行吧……
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
- 如果是下划线ms-hello ,则`host = new URI(url).getHost();`会返回null，
从而导致`org.springframework.cloud.netflix.feign.FeignClientsRegistrar#validate` 抛出校验异常 FeignClients不可用
- **诡异**：将`spring.cloud.config`的配置写在`application.yml`不生效，只能写在`bootstrap.yml`，让我觉得好坑。（原来貌似是可以的）

# 分布式高可用
以下是初步设想方案，还未验证，诸君有兴趣可看架构图。
![分布式服务架构图](https://github.com/Paleozoic/spring_cloud_demo/blob/master/img/%E5%88%86%E5%B8%83%E5%BC%8F%E6%9C%8D%E5%8A%A1%E6%9E%B6%E6%9E%84%E5%9B%BE.png)

# PS
- 网关用于控制对外（但不限制此要求）提供的路由。可用nginx替代（服务端负载均衡）
- 内部服务治理则直接基于Eureka实现服务注册发现，Ribbon（Feign）实现负载均衡（客户端负载均衡）

# TODO
- 转为分布式服务
- 整合更多组件

