package com.ddsmile.dao;

import com.ddsmile.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户数据访问层
 */
/*@Mapper*/
public interface UserMapper {
    /**
     * 根据用户id查询用户
     * @param id 用户id
     * @return
     */
    User selectById(int id);

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return
     */
    User selectByName(String username);

    /**
     * 根据用户邮箱查询用户
     * @param email 用户邮箱
     * @return
     */
    User selectByEmail(String email);

    /**
     * 增加新用户
     * @param user
     * @return
     */
    int insertUser(User user);

    /**
     * 更新用户的
     * @param id 用户id
     * @param status  用户状态  0-未激活; 1-已激活
     * @return
     */
    int updateStatus(int id, int status);

    /**
     * 更新用户头像
     * @param id 用户id
     * @param headerUrl 头像路径
     * @return
     */
    int updateHeader(int id, String headerUrl);

    /**
     * 更新用户密码
     * @param id 用户id
     * @param password 用户密码
     * @return
     */
    int updatePassword(int id, String password);
}
