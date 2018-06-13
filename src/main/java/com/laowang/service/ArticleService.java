package com.laowang.service;

import com.laowang.bean.Article;
import com.laowang.repository.ArticleRepository;
import com.laowang.utils.api.baidu.ClassiferFromBaidu;
import com.laowang.utils.api.baidu.ResultTagBean;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository repository;

    /**
     * 封装文章bean，同时把分类结果也做好
     * @param article
     * @return
     */
    public Article buildeArticle(Article article){
        //防止重复提交分类
        Article temp = repository.findByTitleAndContentAndOwerId(article.getTitle(),article.getContent(),article.getOwerId());
        if (temp != null)
            return temp;
        //
        article.setShare(0);//设置是否分享
        article.setDate(new Date().toString());//设置创建日期
        article.setState(1);//设置是否被删除
        //封装分类标签
        ResultTagBean tag = ClassiferFromBaidu.getArticleTags(article.getTitle(),article.getContent());
        List<String> tags = tag.getLv2_tag();
        article.setTag(tag.getLv1_tag());//设置一级标签
        if (tags != null && tags.size()>0) {
            article.setTag1(tags.get(0));//设置二级标签1
            if (tags.size() > 1)
                article.setTag2(tags.get(1));//设置二级标签2
            if (tags.size() > 2)
                article.setTag3(tags.get(2));//设置二级标签3
        }
        return repository.save(article);
    }

    /**
     * 设置文章分享状态为true
     * @param article
     * @return
     */
    public Article shareArticle(Article article,Integer owerId){
        Article old = repository.getOne(article.getId());
        if (old.getOwerId()!= owerId)
            return null;
        old.setShare(1);
        return repository.save(old);
    }

    /**
     * 删除一篇文章
     * @param article
     * @return
     */
    public Article deleteArticle(Article article,Integer id){
        Article old = repository.getOne(article.getId());
        if (old.getOwerId() != id)
            return null;
//        repository.deleteById(article.getId());
        old.setShare(0);
        old.setState(0);
        return repository.save(old);
    }

    /**
     * 获取用户在云端的所有未被删除的文章
     * @param uid
     * @return
     * //TODO:分页查询功能
     */
    public List<Article> getArticlesByUserId(Integer uid){
        return repository.findAllByOwerIdAndState(uid,1);
    }

    /**
     * 获取用户分享的所有文章
     * @param uid
     * @return
     * //TODO:分页
     */
    public List<Article> getAllSharingArticleByUserId(Integer uid){
        return repository.findAllByShareAndOwerIdAndState(1,uid,1);
    }


    /**
     * 获取所有用户分享的文章
     * @return
     * //TODO：分页功能
     */
    public List<Article> getAllSharingArticles(){
        return repository.findAllByShareAndState(1,1);
    }


    /**
     * 用户获取网络文章分类结果
     * @param url
     * @param uid
     * @return
     */
    public Article userGetNetArticle(String url, Integer uid){
        url = "http://ci.lab317.org/api/v1/extract?url="+url;
        try {
            Document doc = Jsoup.connect(url).ignoreContentType(true).get();
            String result = doc.body().html();
            //用户提交数据将会被记录
            JSONObject jsonObject = new JSONObject(result);
            String content = jsonObject.getString("content");
            String title = jsonObject.getString("title");
            Article article = new Article();
            article.setOwerId(uid);
            article.setTitle(title);
            article.setContent(content);
            return buildeArticle(article);//TODO 要么解析，要么再发送一次网络请求
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 公共接口，获取网络文章分类
     * @param url
     * @return
     */
    public String getNetArticle(String url){
        url = "http://ci.lab317.org/api/v1/extract?url="+url;
        try {
            Document doc = Jsoup.connect(url).ignoreContentType(true).get();
            return doc.body().html();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
