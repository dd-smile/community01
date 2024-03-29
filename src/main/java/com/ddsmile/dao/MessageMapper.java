package com.ddsmile.dao;

import com.ddsmile.entity.Message;

import java.util.List;

public interface MessageMapper {
    /**
     * 查询当前用户的会话列表，针对每个会话只返回一条最新的私信
     * @param userId 用户id
     * @param offset 当前页的起始行
     * @param limit 显示上限
     * @return
     */
    List<Message> selectConversations(int userId, int offset, int limit);

    /**
     * 查询当前用户的会话数量
     * @param userId 用户id
     * @return
     */
    int selectConversationCount(int userId);

    /**
     * 查询某一个会话包含的私信列表
     * @param conversationId 会话的id
     * @param offset 当前页的起始行
     * @param limit 显示上限
     * @return
     */
    List<Message> selectLetters(String conversationId, int offset, int limit);

    /**
     * 查询某一个会话包含的私信数量
     * @param conversationId 会话的id
     * @return
     */
    int selectLetterCount(String conversationId);

    /**
     * 查询未读私信的数量：conversationId传了就拼，不传就不拼了
     * @param userId 用户id
     * @param conversationId 会话的id
     * @return
     */
    int selectLettersUnreadCount(int userId,String conversationId);

    /**
     * 添加私信
     * @param message 私信的信息
     * @return
     */
    int insertMessage(Message message);

    /**
     * 将私信设置为已读（修改消息的状态）
     * @param ids 多个消息的id
     * @param status 消息的状态
     * @return
     */
    int updateStatus(List<Integer> ids, int status);

    /**
     * 查询某个主题下最新的通知
     * @param userId 谁的通知
     * @param topic 传入哪个主题
     * @return
     */
    Message selectLatestNotice(int userId, String topic);

    /**
     * 查询某个主题所包含的通知数量
     * @param userId 谁的通知
     * @param topic 传入哪个主题
     * @return
     */
    int selectNoticeCount(int userId, String topic);

    /**
     * 查询未读的通知的数量
     * @param userId 谁的通知
     * @param topic 传入哪个主题
     * @return
     */
    int selectNoticeUnreadCount(int userId, String topic);

    /**
     * 查询某个主题所包含的通知列表
     * @param userId 谁的通知
     * @param topic 哪个主题
     * @param offset 当前页的起始行
     * @param limit 显示上限
     * @return
     */
    List<Message> selectNotices(int userId, String topic, int offset, int limit); // 需要分页显示


}
