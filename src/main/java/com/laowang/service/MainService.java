package com.laowang.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laowang.utils.GetClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
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

    public String getOneTextClass(String text){
        return getClass.getOneTextClass(text);
    }

    public String getSomeTextClasses(Map map){
        //用来转换post参数，存放文本
        List<String> data = new LinkedList<>();
        for (Object o: map.values()) {
            data.add(((String[])o)[0]);
        }
        String[] text = new String[data.size()];
        for (int i=0;i<data.size();i++){
            text[i] = data.get(i);
        }
        return getClass.getSomeTextClass(text);
    }
}
