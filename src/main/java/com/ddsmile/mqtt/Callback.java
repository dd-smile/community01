package com.ddsmile.mqtt;

import com.alibaba.fastjson.JSONObject;
import com.ddsmile.dao.DataSensorMapper;
import com.ddsmile.entity.DataSensor;
import com.ddsmile.service.DataSensorService;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 常规MQTT回调函数
 *
 */
@Component
public class Callback implements MqttCallback {

    @Autowired
    private DataSensorService dataSensorService;

    DataSensor dataSensor = new DataSensor();

    Map<String,Object> map = new HashMap<>();

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
        JSONObject jsonObject = JSONObject.parseObject(a);
        String temp = jsonObject.getString("Temp");
        String hum = jsonObject.getString("Hum");
        String co2 = jsonObject.getString("Co2");

        System.out.println(temp);
        System.out.println(hum);
        System.out.println(co2);

        //测试map取得到数据吗
        map.put("temp", temp);
        map.put("hum", hum);
        map.put("co2", co2);


        dataSensor.setSensorTemp(temp);
        dataSensor.setSensorHum(hum);
        dataSensor.setSensorCo2(co2);
        dataSensor.setRecordTime(new Date(System.currentTimeMillis() + 1000*60*10));
        dataSensorService.addDataSensor(dataSensor);

    }

    public Map<String, Object> getMap() {
        return map;
    }
}
