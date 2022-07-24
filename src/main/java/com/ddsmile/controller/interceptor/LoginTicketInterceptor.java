package com.ddsmile.controller.interceptor;

import com.ddsmile.entity.LoginTicket;
import com.ddsmile.entity.User;
import com.ddsmile.service.UserService;
import com.ddsmile.util.CookieUtil;
import com.ddsmile.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 拦截器
 * 显示登录信息
 * 1.请求开始时查询登录用户；
 * 2.在本次请求中持有用户数据(存在内存里)；
 * 3.在模板视图上显示用户数据；
 * 4.在请求结束时清理用户数据。
 */

@Component
public class LoginTicketInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;

    //在控制器方法之前执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从cookie中获取凭证
        String ticket = CookieUtil.getValue(request, "ticket");
        if (ticket != null){
            //查询凭证
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            //判断凭证是否有效 : 状态有效、超时状态为在当前时间之前
            if (loginTicket != null && loginTicket.getStatus() ==0 && loginTicket.getExpired().after(new Date())) {
                //根据凭证查询用户id, 然后根据用户id查询凭证
                User user = userService.findUserById(loginTicket.getUserId());
                //在本次请求持有用户：将user存入，后续使用
                //同时需要在多线程之间隔离user
                hostHolder.setUser(user);
            }
        }
        return true;
    }

    //控制器方法结束后，模板引擎之前调用
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if(user != null && modelAndView != null){
            modelAndView.addObject("loginUser", user);
        }
    }

    //模板引擎之后调用
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //模板执行完后，清掉里面的user
        hostHolder.clear();
    }

}
