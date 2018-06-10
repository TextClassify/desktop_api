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

    private static String key= "laowang";

    public static String getUserNameByToken(String token){
        String userName;
        try {
             Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
             userName = claims.getSubject();
        }catch (Exception e) {
            logger.warn("token异常");
            userName = null;
        }
        return userName;
    }


    public static String initToken(String name,String keyGen){
        //生成数据声明claims
        Map<String,Object> claims = new HashMap<>();
        claims.put("sub",name);
        claims.put("keyGen",keyGen);
        String Token = Jwts.builder().setClaims(claims)
                //加密算法
                .signWith(SignatureAlgorithm.HS512,key).compact();
        return Token;
    }

    public static String freshtoken(String name){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<10;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return initToken(name,sb.toString());
    }
}
