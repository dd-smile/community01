package com.ddsmile.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 此类作为业务功能增强。实现日志的功能
 */
@Component
@Aspect
public class ServiceLogAspect {
    private static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

    @Pointcut("execution(* com.ddsmile.service.*.*(..))")//切入点表达式，表示在哪一个包下的哪一个方法进行功能增强
    public void pointCut(){

    }

    /**
     * 定义好切入点表达式，在这里实现业务功能增强，选取连接点，定义哪个方法上边生效
     */
    @Before("pointCut()")
    public void before(JoinPoint joinPoint){//用来获取连接点的位置
        //用户[1,2,3,4],在[xxx]访问了[com.nowcoder.community.service]
        //获取用户ip利用工具类 RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes==null){
            return;
        }
        HttpServletRequest request = requestAttributes.getRequest();
        //获取ip
        String ip = request.getRemoteHost();
        //获取时间
        String now = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());
        //目标的类型名 + 方法名
        String target = joinPoint.getSignature().getDeclaringTypeName() + "." +joinPoint.getSignature().getName();
        logger.info(String.format("用户[%s],在[%s]，访问了[%s]", ip, now, target));
    }
}
