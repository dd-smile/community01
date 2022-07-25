package com.ddsmile.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解。让被注解标识的方法，被拦截。
 */
@Target({ElementType.METHOD}) //表示自己定义的这个注解用于描述方法
@Retention(RetentionPolicy.RUNTIME) //表名程序运行时，自己定义的这个注解才有效
public @interface LoginRequired {
    //只起到标识的作用
    //被该注解标识的方法，需要进行登录验证
}
