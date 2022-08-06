package com.ddsmile.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 触发事件 :
 *   评论后，发布通知
 *   点赞后，发布通知
 *   关注后，发布通知
 * 评论，点赞、关注等系统通知是 并发, 异步的.
 * 可以将其抽象为event事件,(Entity(实体)类转换为Event(事件)类,然后让Kafka处理)
 *
 */
public class Event {

    private String topic;  // 表示event类型(评论\点赞\关注)
    private int userId;      // 表示执行event的用户id
    private int entityType;  // entityType 与 entityId 确定了该topic 的Entity对象(帖子\回复\用户)
    private int entityId;
    private int entityUserId;  // Entity对象的useId
    private Map<String, Object> data = new HashMap<>(); // map 用来存放一些补充数据，比如时间, 具有一定的扩展性

    public String getTopic() {
        return topic;
    }

    /**
     * 带有返回Event的 setXXX() 方法，可以在实例化的时候更灵活点
     * @param topic
     * @return
     */
    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    /**
     * 带有返回Event的 setXXX() 方法，可以在实例化的时候更灵活点
     * @param userId
     * @return
     */
    public Event setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    /**
     * 带有返回Event的 setXXX() 方法，可以在实例化的时候更灵活点
     * @param entityType
     * @return
     */
    public Event setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    /**
     * 带有返回Event的 setXXX() 方法，可以在实例化的时候更灵活点
     * @param entityId
     * @return
     */
    public Event setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityUserId() {
        return entityUserId;
    }

    /**
     * 带有返回Event的 setXXX() 方法，可以在实例化的时候更灵活点
     * @param entityUserId
     * @return
     */
    public Event setEntityUserId(int entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    /**
     * 不让外界直接传一个map进来, 而是传入key值与value值进行封装
     * @param key
     * @param value
     */
    public Event setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}
