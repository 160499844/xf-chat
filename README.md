# 基于websocket加密通信聊天程序

#### 介绍
基于websocket加密通信聊天程序,使用特殊的加密方式，对通信进行加密，防止第三方获取聊天内容，破解聊天记录。
1.创建群聊，需要口令加入群聊中，非群成员无法解密聊天记录。
2.消息退出自动销毁
3.图片消息24小时后无法读取
4.目前仅支持默认表情,文本，图片消息发送。



#### 软件架构
软件架构说明
springboot、netty、websocket、rabbitmq、MongoDB、redis


#### 安装教程

1.  点击清理.bat可以快速清理旧的jar文件,点击打包.bat可以快速生成启动jar文件
2.  方式一：linux环境下,输入nohup java -jar xf-chat-0.0.1-SNAPSHOT.jar &命令启动项目
3.  方式二：windows环境下,打开cmd，输入 java -jar xf-chat-0.0.1-SNAPSHOT.jar 命令启动项目
4.  指定配置文件启动 java -jar xf-chat-0.0.1-SNAPSHOT.jar -Dspring.config.location=/xxx/application.properties，
    配置文件需要同jar同级目录下面,使用绝对路径，参考springboot外置配置文件启动

#### 使用说明
1.  修改配置文件中MongoDB、redis、rabbitmq配置，改成你自己的（重要）
2.  运行XfChatApplication.java,运行项目后输入http:127.0.0.1:8000访问
3.  创建群组，输入6位口令，生成群聊临时链接，复制粘贴给好友
4.  好友复制粘贴群聊临时链接到浏览器,即可访问，输入用户名口令加入群聊

#### 参与贡献

1.  小峰

