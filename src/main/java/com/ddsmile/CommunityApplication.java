package com.ddsmile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ddsmile.controller.DataWatchController;
import com.ddsmile.mqtt.MQTTConnect;
import com.ddsmile.util.ButtonUtil;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.ddsmile.dao"})
public class CommunityApplication {

	public static void main(String[] args) throws MqttException {
		SpringApplication.run(CommunityApplication.class, args);
	}

}
