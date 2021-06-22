# leyou-project

#### 介绍
乐优商城是一个全品类电商购物网站（B2C），目前提供了基于H5的购物网站，基于小程序的客户端正在开发中。服务端基于SpringCloud技术栈搭建微服务群，对外提供统一的REST风格接口，实现对多端的统一支持。

目前国内实施微服务有两套技术解决方案：

- 一种是以阿里的Dubbo为核心的，基于RPC的微服务架构
- 一种是以Spring的SpringCloud的为核心的，基于Rest风格的微服务架构

我们的乐优商城选择了SpringCloud技术栈来构建项目。原因是这样的：

- 可靠：Spring平台一直致力于java技术的研究，平台更加可靠稳定；毕竟dubbo有被阿里“抛弃过”的黑历史。
- 方便：因为是Spring的”亲生儿子“，所以SpringBoot的支持非常完美，简化了系统搭建的工作
- 易上手：大多数程序员接触框架都是从Spring开始，比较熟悉Spring的“味道”，学习SpringCloud也会熟悉的感觉。

#### 软件架构
乐优商城采用了前后端分离的架构方式，

- 前端采用Vue技术栈，从使用者的角度，分为：
  - 前台门户系统：目前只支持H5，后期会加入微信小程序
  - 后台管理系统：基于Vue实现的单页应用（SPA），实现管理功能
- 服务端采用SpringCloud技术栈形成微服务集群，会包括：
  - 商品微服务：商品及商品分类、品牌、库存等的服务
  - 搜索微服务：实现搜索功能
  - 交易微服务：实现订单相关业务、实现购物车相关业务
  - 用户服务：用户的登录注册、用户信息管理等功能
  - 短信服务：完成各种短信的发送任务
  - 支付服务：对接各大支付平台
  - 授权服务：完成对用户的授权、鉴权等功能

