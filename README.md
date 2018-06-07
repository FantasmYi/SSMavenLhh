# SSMavenLhh
## 基于SSM+Maven的分布式网上购物商城
项目采用dubbo框架实现服务提供方和服务消费方的接口调用，达到负载均衡
### 系统后台
商品编辑：商品增删改查，使用了分页插件
=================================
上架：更改商品状态，添加商品信息到solr服务器，静态化商品页面
=================================
### 系统前台
商品检索：使用solr服务器进行全文检索，商品详情页面采用freemarker实现静态化
=================================
购物车：采用cookie和redis进行存储
=================================
### 单点登录系统
基于redis实现session共享




