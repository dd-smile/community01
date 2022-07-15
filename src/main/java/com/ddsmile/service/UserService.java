package com.ddsmile.service;

import com.ddsmile.dao.UserMapper;
import com.ddsmile.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户业务逻辑层
 * 用于把用户id转换成用户名
 */
@Service
public class UserService {
    @Resource
    private UserMapper userMapper;
    //根据id查询一个用户
    public User findUserById(int id){
        return userMapper.selectById(id);
    }
}
