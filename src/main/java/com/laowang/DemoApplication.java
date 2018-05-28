package com.laowang;

import com.laowang.utils.JwtUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.InputStream;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		JwtUtil jwtUtil = new JwtUtil();
		String token = jwtUtil.getTokenByName("laowang");
		System.out.println(token);
		System.out.println(jwtUtil.getClaimsFromToken(token));
	}
}
