package com.laowang.repository;

import com.laowang.bean.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by wangyonghao8 on 2018/6/3.
 */
public interface ArticleRepository extends JpaRepository<Article,Integer> {
    List<Article> findAllByOwerIdAndState(Integer owerId,Integer state, Pageable pageable);
    List<Article> findAllByShareAndState(Integer share,Integer state, Pageable pageable);
    List<Article> findAllByShareAndOwerIdAndState(Integer share,Integer owerid,Integer state, Pageable pageable);
    Article findByTitleAndContentAndOwerId(String title,String content,Integer owerid);
    Article findByIdAndOwerId(Integer id,Integer uid);
    List<Article> findFirst10ByOwerIdAndState(Integer uid, Integer state, Pageable pageable);
}
