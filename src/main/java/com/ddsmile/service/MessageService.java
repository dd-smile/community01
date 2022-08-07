package com.ddsmile.service;

import com.ddsmile.dao.MessageMapper;
import com.ddsmile.entity.Message;
import com.ddsmile.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * 私信列表 业务逻辑层
 */
@Service
public class MessageService {
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;

    /**
     *查询当前用户的会话列表，针对每个会话只返回一条最新的私信
     * @param userId 用户id
     * @param offset 当前页的起始行
     * @param limit 显示上限
     * @return
     */
    public List<Message> findConversations(int userId, int offset, int limit) {
        return messageMapper.selectConversations(userId, offset, limit);
    }

    /**
     *查询当前用户的会话数量
     * @param userId 用户id
     * @return
     */
    public int findConversationCount(int userId) {
        return messageMapper.selectConversationCount(userId);
    }

    /**
     *查询某一个会话包含的私信列表
     * @param conversationId 会话id
     * @param offset 当前页的起始行
     * @param limit 显示上限
     * @return
     */
    public List<Message> findLetters(String conversationId, int offset, int limit) {
        return messageMapper.selectLetters(conversationId, offset, limit);
    }

    /**
     *查询某一个会话包含的私信数量
     * @param conversationId 会话id
     * @return
     */
    public int findLettersCount(String conversationId) {
        return messageMapper.selectLetterCount(conversationId);
    }

    /**
     *查询未读私信的数量：conversationId传了就拼，不传就不拼了
     * @param userId 用户id
     * @param conversationId 会话id
     * @return
     */
    public int findLettersUnreadCount(int userId, String conversationId) {
        return messageMapper.selectLettersUnreadCount(userId, conversationId);
    }

    /**
     * 添加私信
     * @param message 私信的信息
     * @return
     */
    public int addMessage(Message message){
        //首先进行过滤敏感词
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveFilter.filter(message.getContent()));
        return messageMapper.insertMessage(message);
    }

    /**
     * 私信变已读
     * @param ids 多个消息的id
     * @return
     */
    public int readMessage(List<Integer> ids){
        return messageMapper.updateStatus(ids,1);
    }

    /**
     * 查询某个主题下最新的通知
     * @param userId  谁的通知
     * @param topic 传入哪个主题
     * @return
     */
    public Message findLatestNotice(int userId, String topic) {
        return messageMapper.selectLatestNotice(userId, topic);
    }

    /**
     * 查询某个主题所包含的通知数量
     * @param userId 谁的通知
     * @param topic 传入哪个主题
     * @return
     */
    public int findNoticeCount(int userId, String topic) {
        return messageMapper.selectNoticeCount(userId, topic);
    }

    /**
     * 查询未读的通知的数量
     * @param userId 谁的通知
     * @param topic 传入哪个主题
     * @return
     */
    public int findNoticeUnreadCount(int userId, String topic) {
        return messageMapper.selectNoticeUnreadCount(userId, topic);
    }

    /**
     * 查询某个主题所包含的通知列表
     * @param userId 谁的通知
     * @param topic 哪个主题
     * @param offset 当前页的起始行
     * @param limit 显示上限
     * @return
     */
    public List<Message> findNotices(int userId, String topic, int offset, int limit) {
        return messageMapper.selectNotices(userId, topic, offset, limit);
    }


}
