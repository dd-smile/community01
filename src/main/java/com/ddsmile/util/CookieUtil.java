package com.ddsmile.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


/**
 * 封装cookie   -> 从request中取出cookie
 *
 */
public class CookieUtil {

    /**
     * 传入key，从请求中的众多cookie中找到对应的value
     * @param request 请求
     * @param name 要取值(value)的key值
     * @return
     */
    public static String getValue(HttpServletRequest request, String name){
        if (request == null || name == null){
            throw new IllegalArgumentException("参数为空！");
        }
        //获取请求中的全部cookie
        Cookie[] cookies = request.getCookies();
        //防止请求中无cookie
        if(cookies != null){
            for (Cookie cookie : cookies){
                if(cookie.getName().equals(name)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
