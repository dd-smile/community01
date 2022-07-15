package com.ddsmile.dao;

import com.ddsmile.entity.DiscussPost;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 帖子数据访问层
 */
public interface DiscussPostMapper {

    /**
     * 查询用户个人的所有帖子：用户ID、分页的起始行号、每页展示的数目
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    //查询表里有多少条数据：@Param 给参数起别名
    //当SQL语句中需要动态的拼接条件(在<if>里使用)，且方法内只有一个参数时，该参数必须起别名 @Param("userId")

    /**
     * 查询总页数
     * @param userId
     * @return
     */
    int selectDiscussPostRows(@Param("userId")int userId);
}
