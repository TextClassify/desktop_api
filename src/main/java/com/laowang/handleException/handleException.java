package com.laowang.handleException;

import com.laowang.bean.Result;
import com.laowang.utils.ResultUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wangyonghao8 on 2018/4/23.
 */
@ControllerAdvice
public class handleException {
    private static Logger logger = LoggerFactory.getLogger(handleException.class);

    @ExceptionHandler(value = NoHandlerFoundException.class)
    public @ResponseBody Result noHandlerException(Exception e, HttpServletRequest req){
        logger.error("[系统异常]{}", e);
        String uri = req.getRequestURI();
        //如果自定义异常，这里可以稍作区分提示信息
        return ResultUtils.error(3,"请求地址:"+uri+"不存在");
    }

    /**
     * 捕获其他所有异常，同时打印
     * @return 错误提示信息
     */
    @ExceptionHandler(value = Exception.class)
    public  @ResponseBody Result exceptionHandle(Exception e){
        logger.error("[系统异常]{}", e);
        //如果自定义异常，这里可以稍作区分提示信息
        return ResultUtils.error(2,"系统异常，请联系老王");
    }
}
