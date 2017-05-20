# spring-cloud-demo
单机演示版本在single-node分支，对应配置中心仓库是：config-repo。
主分支属于集群版本。目的实现高可用的微服务架构。对应配置中心仓库是：config-repo-cluster。
主分支是较为完整的分布式多实例演示（主要体现在高可用、负载均衡、横向扩展）。
包括配置中心，监控，服务注册与发现，网关，微服务调用，还差熔断器的验证。

# 集群部署准备(单个节点模拟多节点)
## 网关zuul
- 实现动态路由zuul
- 解决zuul的单点问题
## 服务注册中心
- Eureka HA 3个实例两两相互注册
## 分布式配置中心
- 配置中心注册至Eureka，由Eureka实现HA。
## 压测调优
配置各个组件的最优实践参数。


# DevOps
在我的设想中，通过`java -jar xxx.jar  --spring.profiles.active=dev`指定激活的配置文件，通过`--server.port=20001`指定端口号(方便创建多实例)。
然后通过简单的脚本时间多服务器，多实例的自动化部署。还可以做成一个DevOps的可视化系统。这个会在集群模式里添加详细的设计和演示。
当然，还有更加好的DevOps实践，这里不展开讲了（事实上我也不懂~~ :) ）。

# 配置中心
配置中心仓库位于该项目config-repo-cluster分支。
`application.yml`则是公共配置文件。
`ms-admin-dev.yml` 表示`spring.application.name`是`ms-admin`的应用（其实是serviceId，如果不配置，默认是applicationName），`spring.profiles.active`是`dev`的配置文件。
配置文件位于仓库根目录，每个应用的配置通过文件名来区分。也可以新建仓库或者分支实现区分。


# 单机模拟分布式集群环境
- 导入IDE
- 执行父项目Spring-cloud-demo的maven命令，分别是clean和package
- 之后执行命令：（务必顺序执行）
```bash
# 其实在win下命令不好使，就不写win下批处理脚本了。还是一条条执行吧……
cd D:\xxx\xxx\spring-cloud-demo #项目所在目录
# 启动服务注册与发现中心
java -jar 00-ms-registry-discovery/target/ms-registry-discovery-1.0-SNAPSHOT.jar --spring.profiles.active=devMaster --server.port=20000
java -jar 00-ms-registry-discovery/target/ms-registry-discovery-1.0-SNAPSHOT.jar --spring.profiles.active=devBackup1 --server.port=20001
java -jar 00-ms-registry-discovery/target/ms-registry-discovery-1.0-SNAPSHOT.jar --spring.profiles.active=devBackup2 --server.port=20002
# 启动配置中心实例
java -jar 10-ms-config-center/target/ms-config-center-1.0-SNAPSHOT.jar --spring.profiles.active=dev --server.port=20003
java -jar 10-ms-config-center/target/ms-config-center-1.0-SNAPSHOT.jar --spring.profiles.active=dev --server.port=20004
# 启动监控管理
java -jar 20-ms-admin/target/ms-admin-1.0-SNAPSHOT.jar --spring.profiles.active=dev --server.port=20005
# 启动网关(多活分流)
java -jar 30-ms-gateway/target/ms-gateway-1.0-SNAPSHOT.jar --spring.profiles.active=dev --server.port=20006
java -jar 30-ms-gateway/target/ms-gateway-1.0-SNAPSHOT.jar --spring.profiles.active=dev --server.port=20007
# 启动网关管理（动态路由）
java -jar 31-ms-gateway-admin/target/ms-gateway-admin-1.0-SNAPSHOT.jar --spring.profiles.active=dev --server.port=20008
# POST调用 http://localhost:20007/refreshRoute 刷新路由后才可以通过代理访问ms-world
# 启动微服务
java -jar 40-ms-hello/target/ms-hello-1.0-SNAPSHOT.jar --spring.profiles.active=dev --server.port=20009
java -jar 40-ms-hello/target/ms-hello-1.0-SNAPSHOT.jar --spring.profiles.active=dev --server.port=20010
java -jar 41-ms-world/target/ms-world-1.0-SNAPSHOT.jar --spring.profiles.active=dev --server.port=20011
java -jar 41-ms-world/target/ms-world-1.0-SNAPSHOT.jar --spring.profiles.active=dev --server.port=20012
```
# 访问地址
- Spring Boot Admin
  * http://localhost:20005/
- Eureka
  * http://localhost:20000/
  * http://localhost:20001/
  * http://localhost:20002/
- 直接访问配置中心
  * http://localhost:20003/{applicationName}/{profile}/{lable/branch}
  * http://localhost:20003/ms-admin/dev/config-repo-cluster
  * http://localhost:20004/ms-admin/dev/config-repo-cluster
- 直接访问服务
  * http://localhost:20009/hello
  * http://localhost:20010/hello
  * http://localhost:20011/world
  * http://localhost:20012/world
- 代理访问
  * http://localhost:20006/ms-hello/hello
  * http://localhost:20007/ms-hello/hello
  * http://localhost:20006/ms-world/world
  * http://localhost:20007/ms-world/world

# 配置之坑
- 如果是下划线ms-hello ,则`host = new URI(url).getHost();`会返回null，
从而导致`org.springframework.cloud.netflix.feign.FeignClientsRegistrar#validate` 抛出校验异常 FeignClients不可用
- **诡异**：将`spring.cloud.config`的配置写在`application.yml`不生效，只能写在`bootstrap.yml`，让我觉得好坑。（原来貌似是可以的）

# 分布式高可用
理论上可以实现系统强大的横向扩展能力，增加集群的负载和吞吐。
但是，注意**所有涉及到网关层面的设计，只是实现了分流，但是还是存在单点问题的。而关于zuul/nginx的HA，暂行搁置。**
- 方案一：此架构图首要启动配置中心，之后其他应用从配置中心读取配置（但是配置中心需要对反向代理服务器实现HA。PS：此方案已被抛弃。）
![分布式服务架构图](https://github.com/Paleozoic/spring_cloud_demo/blob/master/img/分布式服务架构图.png)
- 方案二：此架构图首要启动服务注册中心，配置中心随后注册到Eureka。（通过Eureka实现配置中心的HA。）
![分布式服务架构图_NEW](https://github.com/Paleozoic/spring_cloud_demo/blob/master/img/分布式服务架构图_NEW.png)
# PS
- 网关用于控制对外（但不限制此要求）提供的路由。可用nginx替代（服务端负载均衡）
- 内部服务治理则直接基于Eureka实现服务注册发现，Ribbon（Feign）实现负载均衡（客户端负载均衡）

# TODO
- 整合更多组件
- 调优
