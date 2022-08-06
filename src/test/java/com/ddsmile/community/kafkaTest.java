package com.ddsmile.community;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * kafka测试类
 */
@SpringBootTest
public class kafkaTest {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Test
    public void testKafka(){
        kafkaProducer.sendMessage("test", "你好");
        kafkaProducer.sendMessage("test", "在吗");

        try {
            Thread.sleep(1000*10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
//生产者   主动的, 想要什么时候发,就什么时候发
@Component
class KafkaProducer{

    @Autowired
    private KafkaTemplate kafkaTemplate;

    /**
     * 生产消息
     * @param topic 消息的主题
     * @param content 消息的内容
     */
    public void sendMessage(String topic,String content){
        kafkaTemplate.send(topic,content);
    }

}

//消费者   被动的,队列中有消息就处理
@Component
class KafkaConsumer{
    /**
     * 取出消息
     * @param record 封装的消息, 通过封装的消息读取
     */
    @KafkaListener(topics = {"test"})
    public void handleMessage(ConsumerRecord record){
        System.out.println(record.value());
    }
}