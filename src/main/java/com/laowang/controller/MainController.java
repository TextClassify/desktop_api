package com.laowang.controller;

import com.laowang.bean.Result;
import com.laowang.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Map;


/**
 * Created by wangyonghao8 on 2018/3/21.
 */
@RestController
@RequestMapping("/api")
public class MainController {

    @Autowired
    private MainService service;

    @RequestMapping(value = "/oneText",method = RequestMethod.POST)
    public @ResponseBody Result getResultByOneText(String text){
        System.out.println("get one text and it is: "+text);
        return service.getOneTextClass(text);
    }

    @RequestMapping(value = "/someText",method = RequestMethod.POST)
    public @ResponseBody Result getResultBySomeText(HttpServletRequest request){
        Map map = request.getParameterMap();
        return service.getSomeTextClasses(map);
    }


    //------------------废物代码------------------------------------
    @RequestMapping("/init")//别请求
    public @ResponseBody String initModel(){
        this.storeFile("classpath:news_model/category","/root/textClassify/model/category");
        this.storeFile("classpath:news_model/lexicon","/root/textClassify/model/lexicon");
        this.storeFile("classpath:news_model/maxFeatures","/root/textClassify/model/maxFeatures");
        this.storeFile("classpath:news_model/model","/root/textClassify/model/model");
        return "init done!";
    }

    @Autowired
    private ResourceLoader resourceLoader;
    public void storeFile(String from,String to){
        Resource resource = resourceLoader.getResource(from);
        try {
            InputStream inputStream = resource.getInputStream();
            byte[] bb = new byte[1024];// 用来存储每次读取到的字节数组
            // 指定要写入文件的缓冲输出字节流
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(to));
            int n;
            while ((n = inputStream.read(bb)) != -1) {
                out.write(bb, 0, n);// 写入到输出流
            }
            out.close();// 关闭流
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
