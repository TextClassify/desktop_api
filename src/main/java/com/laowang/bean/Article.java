package com.laowang.bean;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by wangyonghao8 on 2018/6/3.
 */
@Entity
public class Article {
    @Id
    @GeneratedValue
    private Integer id;//文章id
    private String title;//文章标题
    @Lob
    private String content;//文章内容
    private String tag;//一级分类
    private String tag1;//二级分类
    private String tag2;//二级分类
    private String tag3;//二级分类
    private Integer share;//分享状态
    private Timestamp date;//创建日期
    private Integer state;//是否删除
    private Integer owerId;//文章拥有者
    private String url;//记录网络文章地址
    private String labels;//文章标签
    private Timestamp time;//分享时间(用于排序分页等操作)
    //
    @javax.persistence.Transient
    private String access_token;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", tag='" + tag + '\'' +
                ", tag1='" + tag1 + '\'' +
                ", tag2='" + tag2 + '\'' +
                ", tag3='" + tag3 + '\'' +
                ", share=" + share +
                ", date=" + date +
                ", state=" + state +
                ", owerId=" + owerId +
                ", url='" + url + '\'' +
                ", labels='" + labels + '\'' +
                ", time=" + time +
                ", access_token='" + access_token + '\'' +
                '}';
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTag1() {
        return tag1;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }

    public String getTag2() {
        return tag2;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    public String getTag3() {
        return tag3;
    }

    public void setTag3(String tag3) {
        this.tag3 = tag3;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getShare() {
        return share;
    }

    public void setShare(Integer share) {
        this.share = share;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getOwerId() {
        return owerId;
    }

    public void setOwerId(Integer owerId) {
        this.owerId = owerId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

}
