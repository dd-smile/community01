package com.ddsmile.dao;

import com.ddsmile.entity.DiscussPost;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 帖子数据访问层
 */
public interface DiscussPostMapper {

    /**
     * 查询用户个人的所有帖子
     * @param userId 用户ID
     * @param offset 分页的起始行号
     * @param limit 每页展示的数目(最多展示多少)
     * @return
     */
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    //查询表里有多少条数据：@Param 给参数起别名
    //当SQL语句中需要动态的拼接条件(在<if>里使用)，且方法内只有一个参数时，该参数必须起别名 @Param("userId")

    /**
     * 查询总页数
     * @param userId 用户id
     * @return
     */
    int selectDiscussPostRows(@Param("userId")int userId);

    /**
     * 发布帖子
     * @param discussPost 帖子(类)
     * @return
     */
    int insertDiscussPost(DiscussPost discussPost);

    /**
     * 根据主键查询帖子
     * @param id 帖子id
     * @return
     */
    DiscussPost selectDiscussPostById(int id);
}
