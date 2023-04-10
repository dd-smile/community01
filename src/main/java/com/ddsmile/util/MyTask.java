package com.ddsmile.util;

import com.ddsmile.entity.Scheduled;
import com.ddsmile.mqtt.Callback;
import com.ddsmile.mqtt.MQTTConnect;
import com.ddsmile.mqtt.MQTTListener;
import com.ddsmile.service.ScheduledService;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

/**
 * 定时器任务工具类
 */
@Component
@EnableScheduling
public class MyTask implements SchedulingConfigurer {

    @Autowired
    protected ScheduledService scheduledService;

    @Autowired
    private MQTTListener mqttListener;


    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask( ()-> {
                    try {
                        process();
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                },
                triggerContext -> {
            Scheduled scheduled = scheduledService.findCronById("1");
            String cron = scheduled.getCron();
            if(cron.isEmpty()){
                System.out.println("cron is null");
            }
            return new CronTrigger(cron).nextExecutionTime(triggerContext);
        });
    }

    private void process() throws MqttException {
        mqttListener.getMqttConnect().pub("/mytopic/sub","123",0);   //测试发布功能
        //mqttListener.getMqttConnect().pub("/mytopic/task",,0);     //发布定时任务

        System.out.println("基于接口定时任务");

    }

}
