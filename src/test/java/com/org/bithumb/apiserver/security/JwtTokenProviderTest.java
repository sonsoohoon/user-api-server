package com.org.bithumb.apiserver.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JwtTokenProviderTest {
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Test
    public void tokenTest() {
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_ADMIN");
        String token = jwtTokenProvider.createToken("shson@naver.com",roles);
        String user = jwtTokenProvider.getUserPk(token);
        Authentication auth = jwtTokenProvider.getAuthentication(token);
        System.out.println("token: " + token + " user: " + user + " validate: " + jwtTokenProvider.validateToken(token) + " auth " + auth);
    }

}