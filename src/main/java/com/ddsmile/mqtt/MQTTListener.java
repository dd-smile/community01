package com.ddsmile.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 项目启动 监听主题
 */
@Component
public class MQTTListener implements ApplicationListener<ContextRefreshedEvent> {

    //记日志
    private static final Logger logger = LoggerFactory.getLogger(MQTTListener.class);

    private final MQTTConnect server;
    private final Callback callback;

    MQTTConnect mqttConnect = new MQTTConnect();

    @Autowired
    public MQTTListener(MQTTConnect server,Callback callback) {
        this.callback = callback;
        this.server = server;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //连接MQTT服务器
        try {
            mqttConnect.setMqttClient("admin", "7815csdd.", new Callback());  //用于发布的连接
            server.setMqttClient("admin", "7815csdd.", callback);  //用于订阅的连接
            server.sub("/mytest/pub");
        } catch (MqttException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public MQTTConnect getMqttConnect() {
        return mqttConnect;
    }
}
