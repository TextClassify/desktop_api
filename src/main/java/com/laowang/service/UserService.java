package com.laowang.service;

import com.laowang.bean.User;
import com.laowang.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by wangyonghao8 on 2018/5/21.
 */
@Service
public class UserService {
    @Autowired
    UserRepository repository;

    /**
     * 通过用户名和密码查找用户
     * @param user
     * @return 用户bean或则null
     */
    public User checkLogin(User user){
        User temp =repository.findByUserNameAndPassword(user.getUserName(),user.getPassword());
        if (temp != null){
            temp.setLastLogin(new Date().toString());
            repository.save(temp);
        }
        return temp;
    }

    /**
     * 通过用户名获取用户信息
     * @param name
     * @return 用户bean或则null
     */
    public User findUserByUserName(String name){
        return repository.findByUserName(name);
    }

    /**
     * 通过id获取用户信息
     * @param id
     * @return
     */
    public User findUserById(Integer id){ return repository.getOne(id);}


    /**
     * 用户注册，检查用户是否存在，保存新用户
     * @param user
     * @return
     */
    public User registor(User user){
        //规定用户名不能重复
        User exist = repository.findByUserName(user.getUserName());
        if (exist!=null){
            return null;
        }else{
            user.setLastLogin(new Date().toString());
            return repository.save(user);

        }
    }

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    public User updateUser(User user){
        User old = repository.findByUserName(user.getUserName());
        if (user.getPassword()!=null)old.setPassword(user.getPassword());
        if (user.getEmail()!=null)old.setEmail(user.getEmail());
        if (user.getInformation()!=null)old.setInformation(user.getInformation());
        old.setLastLogin(new Date().toString());
        return repository.save(old);
    }

    /**
     * 删除用户
     * @param id
     */
    public void deleteUserById(Integer id){
        repository.deleteById(id);
    }
}
