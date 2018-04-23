package com.laowang.service;

import com.laowang.bean.Result;
import com.laowang.utils.GetClass;
import com.laowang.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by wangyonghao8 on 2018/3/21.
 */
@Component
public class MainService {

    @Autowired
    private GetClass getClass;

    public Result getOneTextClass(String text){
        Map result = getClass.getOneTextClass(text);
        return result == null ? ResultUtils.error(1,"未知的错误") : ResultUtils.success(result);
    }

    public Result getSomeTextClasses(Map map){
        //用来转换post参数，存放文本
        List<String> data = new LinkedList<>();
        for (Object o : map.values()) {
            data.add(((String[])o)[0]);
        }
        String[] text = new String[data.size()];
        for (int i=0;i<data.size();i++){
            text[i] = data.get(i);
        }
        Map result = getClass.getSomeTextClass(text);
        return result == null ? ResultUtils.error(1,"未知的错误"):ResultUtils.success(result);
    }
}
