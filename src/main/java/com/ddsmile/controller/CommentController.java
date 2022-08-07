package com.ddsmile.controller;

import com.ddsmile.entity.Comment;
import com.ddsmile.entity.DiscussPost;
import com.ddsmile.entity.Event;
import com.ddsmile.event.EventProducer;
import com.ddsmile.service.CommentService;
import com.ddsmile.service.DiscussPostService;
import com.ddsmile.util.CommunityConstant;
import com.ddsmile.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * 评论视图层
 */
@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityConstant {
    @Autowired
    private CommentService commentService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private EventProducer eventProducer;
    @Autowired
    private DiscussPostService discussPostService;

    //插入评论, 对某个帖子进行评论，get请求
    @RequestMapping(path = "/add/{discussPostId}", method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment){
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);

        // 触发评论事件
        Event event = new Event()
                .setTopic(TOPIC_COMMENT)
                .setUserId(hostHolder.getUser().getId())  // 谁触发的event
                .setEntityType(comment.getEntityType()) // 评论的Entity
                .setEntityId(comment.getEntityId())
                .setData("postId", discussPostId);   // 被评论的帖子id

        // EntityUserId要查不同的表
        // ENTITY_TYPE_POST 说对帖子评论；ENTITY_TYPE_COMMENT 表示对评论回复
        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            DiscussPost target = discussPostService.findDiscussPostById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        } else if (comment.getEntityType() == ENTITY_TYPE_COMMENT) {
            Comment target = commentService.findCommentById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }
        eventProducer.fireEvent(event);

        return "redirect:/discuss/detail/" + discussPostId;
    }

}
