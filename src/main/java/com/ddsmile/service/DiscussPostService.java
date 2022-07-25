package com.ddsmile.service;

import com.ddsmile.dao.DiscussPostMapper;
import com.ddsmile.entity.DiscussPost;
import com.ddsmile.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 帖子业务逻辑层
 */
@Service
public class DiscussPostService {

    @Resource
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    /**
     * 返回查到的用户数据
     * @param userId 用户id
     * @param offset 起始行
     * @param limit 每页限制查询的数量
     * @return
     */
    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit){
        return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }

    /**
     * 调用数据访问层,查询总页数
     * @param userId
     * @return
     */
    public int findDiscussPostRows(int userId){
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    /**
     * 发布帖子
     * @param post 要发布的帖子
     * @return
     */
    public int addDiscussPost(DiscussPost post){
        if(post==null){
            throw new IllegalArgumentException("参数不能为空！");
        }
        //转义HTML标记：防止人家发布的内容中包含html的标签，导致破坏页面
        //只用对主题、评论进行转义、过滤操作
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        //过滤敏感词
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));
        return discussPostMapper.insertDiscussPost(post);
    }

    /**
     * 根据ID查询帖子
     * @param id 帖子id
     * @return
     */
    public DiscussPost findDiscussPostById(int id){
        return discussPostMapper.selectDiscussPostById(id);
    }

}
