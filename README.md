# community01
## 项目背景

此项目是来源于2023年毕设作品。与之配套的还有一个[宠物感知系统](https://github.com/dd-smile/sensor_data)。使用IntelliJ IDEA开发。下面介绍一下项目功能描述。

一个基本功能完整的论坛项目。项目主要功能有：基于邮件激活的注册方式，基于MD5加密的密码存储方式，登录功能加入了随机验证码的验证。实现登录状态检查、为游客与已登录用户展示不同界面与功能。支持用户上传头像，实现发布帖子、评论帖子、发送私信与点赞、关注、节点数据查看等功能。

本项目的技术栈为：

- 编程语言：Java语言，HTML。
- 数据库：MySQL（版本8），Redis。
- 通信协议：MQTT。
- 第三方框架：Kafka。
- 整体架构：SpringBoot（版本2.7.1）。

架构图如下：
![宠物论坛系统架构图](https://github.com/dd-smile/community01/assets/60083638/c6c4812f-1bb5-4c55-a4da-5c97756ec0e1)


环境搭建：

- MQTT服务器：基于EMQX的MQTT服务器。
- 腾讯云服务器：轻量应用服务器（2核4G-60G    Centos7系统）
- idea版本为2020.2。

## 如何使用

本项目为三层架构中的应用层部分，需要配套感知层部分[宠物感知系统](https://github.com/dd-smile/sensor_data)方可使用。

修改配置：

- 修改mqtt包下的MQTT服务器与客户端配置。
- 修改util包下的MyTask类中的MQTT服务器配置。
- 修改application.properties配置。
  - 分为develop与produce。
- 修改logback-spring。xml配置。
  - 分为develop与produce。
- 测试方法在test文件中，可以查看我写的一些测试用例。


