package com.laowang.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangyonghao8 on 2018/4/24.
 */
public class JwtUtil {
    Key key = MacProvider.generateKey();

    public String getTokenByName(String name){
        Map map = new HashMap();
        map.put("name",name);
        return Jwts.builder().setClaims(map).signWith(SignatureAlgorithm.HS512,key).compact();
    }

    public Claims getClaimsFromToken(String token){
        Claims claims;
        try {
             claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        }catch (io.jsonwebtoken.SignatureException e) {

            //don't trust the JWT!  请求发送到登陆界面
            claims = null;
        }
        return claims;
    }
}
