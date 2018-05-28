package com.laowang.service;

import com.laowang.bean.Result;
import com.laowang.bean.User;
import com.laowang.utils.ResultUtils;
import org.springframework.stereotype.Service;

/**
 * Created by wangyonghao8 on 2018/5/21.
 */
@Service
public class UserService {
    //private DAO dao;
    public Result checkLogin(User user){
        //dao.find();
        if("admin".equals(user.getUserName()))
            return ResultUtils.error(10,"登录失败");
        else
            return  ResultUtils.success(user.getUserName());
    }

    public Result registor(User user){
//        if(dao.find())
        if("admin".equals(user.getUserName()))
            return ResultUtils.error(10,"注册失败");
        else
            return  ResultUtils.success(user.getUserName());
    }
}
