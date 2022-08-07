package com.ddsmile.event;

import com.alibaba.fastjson.JSONObject;
import com.ddsmile.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * 生产者
 */
@Component
public class EventProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    /**
     * 处理事件
     * event.getTopic() 得到topic；
     * JSONObject.toJSONString(event)) 将event转化成json字符串直接作为context
     * @param event 触发的事件
     */
    public void fireEvent(Event event){
        //将事件发布到指定的主题
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));//event.getTopic() 得到topic；JSONObject.toJSONString(event)) 将event转化成json字符串直接作为context
    }
}
