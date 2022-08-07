package com.ddsmile.event;

import com.alibaba.fastjson.JSONObject;
import com.ddsmile.entity.Event;
import com.ddsmile.entity.Message;
import com.ddsmile.service.MessageService;
import com.ddsmile.util.CommunityConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 消费者
 */
@Component
public class EventConsumer implements CommunityConstant {

    //记日志
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    private MessageService messageService;

    /**
     * 取出消息
     * @param record 用于接收消息
     */
    @KafkaListener(topics = {TOPIC_COMMENT, TOPIC_LIKE, TOPIC_FOLLOW}) // 该方法监听3个topic
    public void handleCommentMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            logger.error("消息的内容为空!");
            return;
        }

        Event event = JSONObject.parseObject(record.value().toString(), Event.class); // json字符串解析为 Event实例
        if (event == null) {
            logger.error("消息格式错误!");
            return;
        }

        // 发送站内通知
        // 系统发送“ userId +关注/点赞/评论event.getTopic()+ XXX，+链接”
        Message message = new Message();
        message.setFromId(SYSTEM_USER_ID);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());  // 直接将topic作为ConversationId, 不必在使用from_id与to_id拼接, 而是使用主题
        message.setCreateTime(new Date());

        // 剩下的一些数据放在map，生成josn字符串放在 message.setContent
        Map<String, Object> content = new HashMap<>();
        content.put("userId", event.getUserId());
        content.put("entityType", event.getEntityType());
        content.put("entityId", event.getEntityId());

        if (!event.getData().isEmpty()) {
            for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
                content.put(entry.getKey(), entry.getValue());
            }
        }

        //内容是json字符串, 包含了我们要拼这句话的各种条件
        message.setContent(JSONObject.toJSONString(content));
        messageService.addMessage(message);
    }

}
