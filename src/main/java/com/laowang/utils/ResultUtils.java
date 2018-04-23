package com.laowang.utils;

import com.laowang.bean.Result;

/**
 * Created by wangyonghao8 on 2018/4/23.
 */
public class ResultUtils {

    /**
     *返回结果实体的success封装
     * @param var 返回的结果数据
     * @return http请求成功返回的最外层对象
     */
    public static Result success(Object var){
        Result result = new Result();
        result.setCode(0);//成功返回0
        result.setMsg("成功.");
        result.setData(var);
        return result;
    }

    public static Result success(){return success(null);}

    /**
     * 返回请求失败的error封装
     * @param code 错误代码
     * @param msg 错误信息
     * @return http请求失败返回的最外层对象
     */
    public static Result error(Integer code,String msg){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }

}
