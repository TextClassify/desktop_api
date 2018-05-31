package com.laowang.service;

import com.laowang.bean.Result;
import com.laowang.bean.User;
import com.laowang.repository.UserRepository;
import com.laowang.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wangyonghao8 on 2018/5/21.
 */
@Service
public class UserService {
    @Autowired
    UserRepository repository;

    public User getUserById(Integer id){
        return repository.getOne(id);
    }

    public User getUserByNameAndPassword(String name,String password){
        return repository.findByUserNameAndPassword(name,password);
    }

    public User addUser(User user){
        return repository.save(user);
    }

    public void delete(Integer id){
        repository.deleteById(id);
    }

    public User update(User user){
        User old = repository.getOne(user.getId());
        if (user.getUserName() != null) old.setUserName(user.getUserName());
        if (user.getEmail() != null) old.setEmail(user.getEmail());
        if (user.getPassword() != null) old.setPassword(user.getPassword());
        return repository.save(old);
    }
    //=============================================================================================
    //         上面是DAO 下面是业务层
    //=============================================================================================
    public Result checkLogin(User user){
        if (this.getUserByNameAndPassword(user.getUserName(),user.getPassword()) != null){
            return  ResultUtils.success(user.getUserName()+"登陆成功");
        }else{
            return ResultUtils.error(10,"登陆失败");
        }
    }

    public Result registor(User user){
        User exist = repository.findByUserName(user.getUserName());
        if(exist!= null){
            return ResultUtils.error(11,"用户已存在");
        }else{
            repository.save(user);
            return ResultUtils.success(user.getUserName()+"注册成功");
        }
    }
}
