package com.ddsmile.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * 项目启动 监听主题
 *
 * @author Mr.Qu
 * @since 2022/11/25
 */
public class MQTTListener implements ApplicationListener<ContextRefreshedEvent> {

    //记日志
    private static final Logger logger = LoggerFactory.getLogger(MQTTListener.class);

    private final MQTTConnect server;

    @Autowired
    public MQTTListener(MQTTConnect server) {
        this.server = server;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            server.setMqttClient("admin", "public", new Callback());
            server.sub("com/iot/init");
        } catch (MqttException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
