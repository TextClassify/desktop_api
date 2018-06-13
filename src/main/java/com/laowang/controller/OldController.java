package com.laowang.controller;

import com.laowang.bean.Article;
import com.laowang.bean.Result;
import com.laowang.bean.User;
import com.laowang.repository.ArticleRepository;
import com.laowang.service.ArticleService;
import com.laowang.service.MainService;
import com.laowang.service.UserService;
import com.laowang.utils.JwtUtil;
import com.laowang.utils.ResultUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.Map;


/**
 * Created by wangyonghao8 on 2018/3/21.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class OldController {

    private final static Logger logger = LoggerFactory.getLogger(OldController.class.getName());

    @Autowired
    private MainService service;

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleRepository articleRepository;

    @RequestMapping(value = "/oneText",method = RequestMethod.POST)
    public @ResponseBody Result getResultByOneText(@RequestBody Article article,
                                                   HttpServletRequest request){
        Result result = service.getOneTextClass(article.getContent());
        //如果有用户，这里接口应该还需要用户名参数用来提高token的安全性，降低被别人成功盗用的可能性
        String token = request.getHeader("Authorization");
        if(token != null && !"".equals(token)){
            String name = JwtUtil.getUserNameByToken(token.replace("Bearer ", ""));
            if(name != null && !"".equals(name)){
                logger.info("有token请求，用户是"+name);
                User user = userService.findUserByUserName(name);
                if (user != null){
                    article.setOwerId(user.getId());
                    article.setDate(new Date().toString());
                    article.setShare(0);
                    article.setTag(result.getData().toString());
                    articleRepository.save(article);
                }
            }else{
                result.setMsg("token异常，正常提供服务");
            }
        }
        return result;
    }

    @GetMapping("/share")
    public @ResponseBody String shareArticle(){
        return "对不起，现在不能提供分享功能";
    }

    @RequestMapping(value = "/someText",method = RequestMethod.POST)
    public @ResponseBody Result getResultBySomeText(@RequestBody Map jsonMap){
        if (jsonMap != null)
            System.out.println(jsonMap);
        return service.getSomeTextClasses(jsonMap);
    }

    @PostMapping(value = "/login")
    public @ResponseBody  Result userLogin(@RequestBody User user, HttpServletResponse response){
        if (userService.checkLogin(user) != null){
            String token = JwtUtil.initToken(user,"");
            response.addHeader("Authorization", "Bearer " + token);
            return ResultUtils.success(user.getUserName()+"登陆成功");
        }
        return ResultUtils.error(233,"用户名或密码错误");
    }

    @PostMapping(value = "/register")
    public @ResponseBody User userRegister(@RequestBody User user){
        return userService.registor(user);
    }

    @Autowired
    ArticleService articleService;
    @GetMapping("/netArticle")
    public @ResponseBody String getNetArticleTag(@RequestParam String url){
        return articleService.getNetArticle(url);
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
