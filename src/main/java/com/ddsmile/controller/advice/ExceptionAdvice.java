package com.ddsmile.controller.advice;

import com.ddsmile.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *   @ControllerAdvice
 *   用于修饰类, 表示该类是Controller的全局配置类
 *   在此类中,可以对Controller进行如下三种全局变量 :
 *   1.异常处理方案 2.绑定数据方案 3.绑定参数方案
 *
 *   @ExceptionHandler
 *   - 用于修饰方法, 该方法会在Controller出现异常后调用, 用于处理捕获到的异常
 *   @ModelAttribute
 *   - 用于修饰方法, 该方法会在Controller方法执行前被调用, 用于Model对象绑定参数
 *   @DataBinder
 *   - 用于修饰方法, 该方法会在Controller方法执行前被调用, 用于绑定参数的转换器
 *
 */
@ControllerAdvice(annotations = Controller.class) //只扫描带 Controller 注解的组件
public class ExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler({Exception.class})
    public void handlerException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.error("服务器发送异常: " +e.getMessage());
        for (StackTraceElement element : e.getStackTrace()){
            logger.error(element.toString());
        }
        //判断请求的类型，普通请求、异步请求（返回的为JSON）
        String xRequestedWith = request.getHeader("x-requested-with");
        if ("XMLHttpRequest".equals(xRequestedWith)){
            //说明这是一个要求json的请求
            response.setContentType("application/plain;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(CommunityUtil.getJSONString(1,"服务器异常"));
        }else {
            //普通请求：重定向到错误页面
            response.sendRedirect(request.getContextPath()+"/error");
        }
    }
}
