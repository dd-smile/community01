package com.ddsmile.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 常规MQTT回调函数
 *
 * @author Mr.Qu
 * @since 2022-11-25 15:12
 */
public class Callback implements MqttCallback {

    //记日志
    private static final Logger logger = LoggerFactory.getLogger(Callback.class);

    /**
     * MQTT 断开连接会执行此方法
     */
    @Override
    public void connectionLost(Throwable throwable) {
        logger.info("断开了MQTT连接 ：{}", throwable.getMessage());
        logger.error(throwable.getMessage(), throwable);
    }

    /**
     * publish发布成功后会执行到这里
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        logger.info("发布消息成功");
    }

    /**
     * subscribe订阅后得到的消息会执行到这里
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        //      此处可以将订阅得到的消息进行业务处理、数据存储
        logger.info("收到来自 " + topic + " 的消息：{}", new String(message.getPayload()));
        String a = new String(message.getPayload());
        System.out.println("收到来自 " + topic + " 的消息：{}");
        System.out.println(a);
    }

}
