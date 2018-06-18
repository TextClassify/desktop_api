package com.laowang.service;

import com.laowang.aspect.HttpAspect;
import com.laowang.bean.Article;
import com.laowang.repository.ArticleRepository;
import com.laowang.utils.api.baidu.ClassiferFromBaidu;
import com.laowang.utils.api.baidu.ResultTagBean;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class ArticleService {
    private final static Logger logger = LoggerFactory.getLogger(HttpAspect.class.getName());
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
        if (temp != null) {
            logger.info("用户"+article.getOwerId()+"重复提交待分类文章");
            return temp;
        }
        //
        article.setShare(0);//设置是否分享
        article.setDate(new Timestamp(new Date().getTime()));//设置创建日期
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
        //封装labels
        article.setLabels(tag.getLabels());
        return repository.save(article);
    }

    /**
     * 设置文章分享状态为true
     * @param article
     * @return
     */
    public Article shareArticle(Article article,Integer owerId){
        Article old = repository.getOne(article.getId());
        if (old.getOwerId()!= owerId)//当前用户没有权限分享
            return null;
        old.setShare(1);//表示分享状态
        old.setTime(new Timestamp(new Date().getTime()));//设置分享日期
        return repository.save(old);
    }

    /**
     * 取消分享文章
     * @param article
     * @param owerId
     * @return
     */
    public Article cancleShare(Article article,Integer owerId){
        Article old = repository.getOne(article.getId());
        if (old.getOwerId()!= owerId)
            return null;
        old.setShare(0);
        old.setTime(null);
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
        old.setTime(null);
        return repository.save(old);
    }

    /**
     * 获取用户在云端的所有未被删除的文章
     * @param uid
     * @return
     * //分页查询功能
     */
    public List<Article> getArticlesByUserId(Integer uid,Integer page,Integer state){
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page,10,sort);
        return repository.findAllByOwerIdAndState(uid,state,pageable);
    }



    /**
     * 获取用户分享的所有文章
     * @param uid
     * @return
     * //分页
     */
    public List<Article> getAllSharingArticleByUserId(Integer uid,Integer page){
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page,10,sort);
        return repository.findAllByShareAndOwerIdAndState(1,uid,1,pageable);
    }


    /**
     * 获取所有用户分享的文章
     * @return
     * //分页功能
     */
    public List<Article> getAllSharingArticles(Integer page){
        Sort sort = new Sort(Sort.Direction.DESC, "date");
        Pageable pageable = new PageRequest(page,10,sort);
        return repository.findAllByShareAndState(1,1,pageable);
    }


    /**
     * 用户获取网络文章分类结果
     * @param urlDestination
     * @param uid
     * @return
     */
    public Article userGetNetArticle(String urlDestination, Integer uid){
        String url = "http://ci.lab317.org/api/v1/extract?url="+urlDestination;
        try {
            Document doc = Jsoup.connect(url).ignoreContentType(true).get();
            String result = doc.body().html();
            //用户提交数据将会被记录
            JSONObject jsonObject = new JSONObject(result);
            String content = jsonObject.getString("content");
            String title = jsonObject.getString("title");
            Article article = new Article();
            article.setOwerId(uid);
            article.setUrl(urlDestination);
            article.setTitle(title);
            article.setContent(content);
            //TODO 解析items
            return buildeArticle(article);
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

    public List<Article> getLimitArticles(Integer uid,int page){

//        Pageable pageable = new QPageRequest(0,10,sort);
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(1,10,sort);
        return repository.findFirst10ByOwerIdAndState(uid,1,pageable);
    }

    /**
     * 检查文章id和用户匹配
     * @param id
     * @param uid
     * @return
     */
    public Article checkArticleBelongUser(Integer id,Integer uid){
        Article article = repository.findByIdAndOwerId(id,uid);
        return article;
    }

    /**
     * 还原回收站文章
     * @param id
     * @param uid
     * @return
     */
    public Article reductionArticle(Integer id, Integer uid){
        Article article = repository.getOne(id);
        if (article == null || article.getOwerId() != uid)//文章不存在或则权限不够
            return null;
        article.setState(1);
        return repository.save(article);
    }

    /**
     * 永久删除回收站文章
     * @param id
     * @param uid
     * @return
     */
    public boolean delete(Integer id,Integer uid){
        Article article = repository.getOne(id);
        if (article == null || article.getOwerId() != uid)//文章不存在或则权限不够
            return false;
        repository.deleteById(id);
        return true;
    }

}
