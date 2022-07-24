package com.ddsmile.util;

import com.ddsmile.entity.User;
import org.springframework.stereotype.Component;

/**
 * 在发送请求后，为了持续持有用户信息，并考虑到线程问题
 * 通过ThreadLocal来进行隔离 -> 持有用户信息，用来代替session对象
 */
@Component
public class HostHolder {

    private ThreadLocal<User> users = new ThreadLocal<>();

    /**
     * 外界传进user,存到当前线程的map中
     * @param user 用户信息
     */
    public void setUser(User user){
        users.set(user);
    }

    /**
     * 从ThreadLocal中获取存储的用户信息
     * @return
     */
    public User getUser(){
        return users.get();
    }

    /**
     * 在请求结束时清理用户数据
     */
    public void clear(){
        users.remove();
    }
}
