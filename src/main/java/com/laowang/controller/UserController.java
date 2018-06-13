package com.laowang.controller;

import com.laowang.bean.Article;
import com.laowang.bean.Result;
import com.laowang.bean.User;
import com.laowang.service.ArticleService;
import com.laowang.service.UserService;
import com.laowang.utils.JwtUtil;

import com.laowang.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService service;

    @Autowired
    private ArticleService articleService;

    /**
     * 用户登陆接口
     * @param user 用户名和密码
     * @return token +  用户个人信息
     */
    @PostMapping("/login")
    public @ResponseBody Result handleLogin(@RequestBody User user){
        User tempUser =service.checkLogin(user);
        if (tempUser != null){
            //keyGen用来保证每次登陆token不一样，因为用户不用经常登录，不设置过期时间
            Double keyGen = Math.random();
            String token = JwtUtil.initToken(tempUser,keyGen.toString());
            Map map = new HashMap();
            map.put("access_token",token);
            map.put("user",tempUser);
            //TODO:返回用户前十篇文章
            map.put("articles",articleService.getArticlesByUserId(tempUser.getId()));
            return ResultUtils.success(map,"登陆成功");

        }
        return ResultUtils.error(10,"用户名或密码错误");
    }

    /**
     * 用户注册接口
     * @param user 用户名、密码、邮箱和个性签名
     * @return token+ 个人信息
     */
    @PostMapping("/register")
    public @ResponseBody Result handleRegister(@RequestBody User user){
        User temp =service.registor(user);
        if (temp != null){
            Double keyGen = Math.random();
            Map map = new HashMap();
            map.put("access_token",JwtUtil.initToken(temp,keyGen.toString()));
            map.put("user",temp);
            return ResultUtils.success(map,"注册成功");
        }
        return ResultUtils.error(11,"用户名已存在");
    }

    /**
     * 更新用户信息接口
     * @param user
     * @return 提示信息+ 个人信息
     */
    @PostMapping("/update")
    public @ResponseBody Result handleUpdate(@RequestBody User user){
        String userName = JwtUtil.getUserNameByToken(user.getAccess_token());
        Integer uid = JwtUtil.getUserIdByToken(user.getAccess_token());
        if (userName == null)
            return ResultUtils.error(12,"token不合法");
        if (!service.findUserById(uid).getUserName().equals(user.getUserName()))
            return  ResultUtils.error(15,"用户名和token不一致");
        Map map = new HashMap();
        map.put("user",service.updateUser(user));
        return ResultUtils.success(map,"更新用户信息成功");//返回用户数据
    }

    /**
     * 用户获取一篇文章分类
     * @param article 文章标题、文章内容和token
     * @return 包含文章id的实体
     */
    @PostMapping("/oneText")
    public @ResponseBody Result getOneArticleTag(@RequestBody Article article){
        //先验证token合法性
        String userName = JwtUtil.getUserNameByToken(article.getAccess_token());
        if (userName==null)
            return ResultUtils.error(12,"token不合法");
        Map map = new HashMap();
        //设置文章所有者
        article.setOwerId(JwtUtil.getUserIdByToken(article.getAccess_token()));//设置用户id
        map.put("article",articleService.buildeArticle(article));
        return ResultUtils.success(map,"成功分类");
    }

    /**
     * 分享一篇文章接口
     * @param article 文章id和token
     * @return 分享成功提示信息+ 文章信息
     */
    @PostMapping("/shareArticle")
    public @ResponseBody Result shareArticle(@RequestBody Article article){
        //先验证token合法性
        Integer owerId = JwtUtil.getUserIdByToken(article.getAccess_token());
        if (owerId==null)
            return ResultUtils.error(12,"token不合法");
        //在验证文章拥有者是否正确
        Article goal =articleService.shareArticle(article,owerId);
        if (goal == null){
            return ResultUtils.error(13,"你没有权限分享这篇文章");
        }
        Map map = new HashMap();
        map.put("article",goal);
        return ResultUtils.success(goal,"分享成功");
    }

    /**
     * 删除文章接口
     * @param article 文章id和token
     * @return 提示信息
     */
    @PostMapping("/deleteArticle")
    public @ResponseBody Result deleteArticle(@RequestBody Article article){
        //先验证token合法性
        Integer uid = JwtUtil.getUserIdByToken(article.getAccess_token());
        if (uid==null)
            return ResultUtils.error(12,"token不合法");
        if (articleService.deleteArticle(article,uid) == null){
            return ResultUtils.error(14,"你没有权限删除这篇文章");
        }
        return ResultUtils.success(null,"删除成功");
    }

    /**
     * 获取所有用户分享的文章
     * @param article token
     * @return 所有用户分享状态的文章集合
     * //TODO：分页控制
     */
    @PostMapping("/allSharingArticles")
    public @ResponseBody Result getAllSharingArticles(@RequestBody Article article){
        //验证token的有效性
        String userName = JwtUtil.getUserNameByToken(article.getAccess_token());
        if (userName==null)
            return ResultUtils.error(12,"token不合法");
        Map map = new HashMap();
        map.put("articles",articleService.getAllSharingArticles());
        return ResultUtils.success(map);
    }

    /**
     * 获取当前用户分享的所有文章，用于（批量）修改等操作
     * @param user token和用户名
     * @return 当前用户分享状态的文章集合
     * //TODO:分页
     */
    @PostMapping("/allUserSharedArticles")
    public @ResponseBody Result getUserSharedArticles(@RequestBody User user){
        String name = JwtUtil.getUserNameByToken(user.getAccess_token());
        Integer uid = JwtUtil.getUserIdByToken(user.getAccess_token());
        if (uid == null )//检查token
            return ResultUtils.error(12,"token不合法");
        if (!name.equals(user.getUserName()))
            return ResultUtils.error(15,"用户名和token不一致");
        Map map = new HashMap();
        map.put("articles",articleService.getAllSharingArticleByUserId(uid));
        return ResultUtils.success(map);
    }

    /**
     * 获取指定户名下的所有分享文章，开放接口
     * @param user 用户名
     * @return 用户名下的所有分享状态文章
     */
    @PostMapping("/userSharingArticles")
    public @ResponseBody Result getUserSharingArticles(@RequestBody User user){
        user = service.findUserByUserName(user.getUserName());
        Map map = new HashMap();
        map.put("articles",articleService.getArticlesByUserId(user.getId()));
        return ResultUtils.success(map);
    }

    /**
     * 获取用户所有云端文章
     * @param user 用户名和token
     * @return 当前用户云端的文章集合
     */
    @PostMapping("/allUserArticles")
    public @ResponseBody Result getAllUserArticles(@RequestBody User user){
        String name = JwtUtil.getUserNameByToken(user.getAccess_token());
        Integer uid = JwtUtil.getUserIdByToken(user.getAccess_token());
        if (uid == null )//检查用户和token一致性
            return ResultUtils.error(12,"token不合法");
        if (!name.equals(user.getUserName()))
            return ResultUtils.error(15,"用户名和token不一致");
        Map map = new HashMap();
        map.put("articles",articleService.getArticlesByUserId(uid));
        return ResultUtils.success(map);
    }

    //TODO:批量文章分享

    //TODO:批量删除文章
}


