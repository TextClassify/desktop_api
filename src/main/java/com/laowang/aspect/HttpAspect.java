package com.laowang.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by wangyonghao8 on 2018/3/27.
 */
@Aspect
@Component
public class HttpAspect {
    private final static Logger logger = LoggerFactory.getLogger(HttpAspect.class.getName());

    //定义一个切面，后面好引用
    /**功能：controller包下的所有类的所有public方法的调用需要被记录*/
    @Pointcut("execution(public * com.laowang.controller.*.*(..))")
    public void log(){}

    @Before("log()")
    public void before(JoinPoint point){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        logger.info("======请求开始======");
        //url
        logger.info("url : {}",request.getRequestURL());
        //method
        logger.info("method : {}",request.getMethod());
        //ip
        logger.info("ip : {}",request.getRemoteAddr());
        //类方法
        logger.info("class_method : {}",point.getSignature().getDeclaringTypeName()+"."+point.getSignature().getName());
        //参数
        logger.info("args : {}",point.getArgs());
    }

    @After("log()")
    public void after(){
        //logger.info("没有啥要记录的");
    }

    @AfterReturning(returning = "object",pointcut = "log()")
    public void afterReturn(Object object){
        logger.info("response : {}",object);
        logger.info("======请求结束======");
    }
}
