package com.laowang.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.laowang.bean.Result;
import com.laowang.bean.User;
import com.laowang.service.MainService;
import com.laowang.service.UserService;
import com.laowang.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Map;


/**
 * Created by wangyonghao8 on 2018/3/21.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class MainController {

    @Autowired
    private MainService service;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/oneText",method = RequestMethod.POST)
    public @ResponseBody Result getResultByOneText(@RequestBody String text){
        return service.getOneTextClass(text);
    }

    @RequestMapping(value = "/someText",method = RequestMethod.POST)
    public @ResponseBody Result getResultBySomeText(@RequestBody Map jsonMap){
        if (jsonMap != null)
            System.out.println(jsonMap);
        return service.getSomeTextClasses(jsonMap);
    }

    @PostMapping(value = "/login")
    public @ResponseBody  Result userLogin(@RequestBody User user){
        return userService.checkLogin(user);
    }

    @PostMapping(value = "/register")
    public @ResponseBody Result userRegister(@RequestBody User user){
        return userService.registor(user);
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
