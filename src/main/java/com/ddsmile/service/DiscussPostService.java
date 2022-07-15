package com.ddsmile.service;

import com.ddsmile.dao.DiscussPostMapper;
import com.ddsmile.entity.DiscussPost;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 帖子业务逻辑层
 */
@Service
public class DiscussPostService {

    @Resource
    private DiscussPostMapper discussPostMapper;

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

}
