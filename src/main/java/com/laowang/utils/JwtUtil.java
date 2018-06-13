package com.laowang.utils;

import com.laowang.bean.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by wangyonghao8 on 2018/4/24.
 */
@Component
public class JwtUtil {
    private final static Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    //token密钥
    private static String key= "laowang";

    /**
     * 从token提取用户名
     * @param token
     * @return
     */
    public static String getUserNameByToken(String token){
        String userName;
        try {
             Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
             userName = claims.getSubject();
        }catch (Exception e) {
            logger.warn("token异常,name");
            userName = null;
        }
        return userName;
    }


    /**
     * 从token提取用户id
     * @param token
     * @return
     */
    public static Integer getUserIdByToken(String token){
        Integer id;
        try {
            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
            id = claims.get("uid",Integer.class);
        }catch (Exception e) {
            logger.warn("token异常,id");
            id = null;
        }
        return id;
    }

    /**
     * 创建token
     * @param user
     * @param keyGen
     * @return
     */
    public static String initToken(User user,String keyGen){
        //生成数据声明claims
        Map<String,Object> claims = new HashMap<>();
        claims.put("sub",user.getUserName());
        claims.put("uid",user.getId());
        claims.put("keyGen",keyGen);
        String Token = Jwts.builder().setClaims(claims)
                //加密算法
                .signWith(SignatureAlgorithm.HS512,key).compact();
        return Token;
    }

}
