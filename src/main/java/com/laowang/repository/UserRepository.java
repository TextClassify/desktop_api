package com.laowang.repository;

import com.laowang.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by wangyonghao8 on 2018/5/31.
 */
public interface UserRepository extends JpaRepository<User,Integer>{
    User findByUserName(String name);
    User findByUserNameAndPassword(String userName,String password);
}
