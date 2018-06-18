package com.laowang.bean;

/**
 * 统一请求返回的最外层对象
 * Created by wangyonghao8 on 2018/4/23.
 */
public class Result<T> {

    //结果代码
    private Integer code;
    //结果信息
    private String msg;
    //结果数据
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T date) {
        this.data = date;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
