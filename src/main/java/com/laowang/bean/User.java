package com.laowang.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by wangyonghao8 on 2018/5/21.
 */
@Entity
public class User {
    @Id
    @GeneratedValue
    private Integer id;//用户id
    private String userName;//用户名
    private String password;//用户密码
    private String email;//用户邮箱
    private String information;//用户个人介绍
    private String lastLogin;//最后一次登录时间
    //
    @javax.persistence.Transient
    private String access_token;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", information='" + information + '\'' +
                ", lastLogin='" + lastLogin + '\'' +
                ", access_token='" + access_token + '\'' +
                '}';
    }
}
