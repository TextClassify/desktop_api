package com.laowang.repository;

import com.laowang.bean.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by wangyonghao8 on 2018/6/3.
 */
public interface ArticleRepository extends JpaRepository<Article,Integer> {
    List<Article> findByOwerId(Integer owerId);
}
