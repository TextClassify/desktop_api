package com.laowang.service;

import com.laowang.bean.Article;
import com.laowang.repository.ArticleRepository;
import com.laowang.utils.api.baidu.ClassiferFromBaidu;
import com.laowang.utils.api.baidu.ResultTagBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

}
