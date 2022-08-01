package com.ddsmile.dao;

import com.ddsmile.entity.Comment;

import java.util.List;

/**
 * 评论表的数据访问层
 */
public interface CommentMapper {
    //查询评论表, 进行分页
    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    int selectCountByEntity(int entityType, int entityId);

    //增加评论
    int insertComment(Comment comment);

}
