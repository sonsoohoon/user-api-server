package com.org.bithumb.apiserver.domain.repository;

import com.org.bithumb.apiserver.domain.entity.PctUserEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class PctUserRepositoryTest {
    @Autowired
    PctUserRepository pctUserRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void createTest() {
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_ADMIN");
        PctUserEntity pctUserEntity = PctUserEntity.builder()
                .name("john")
                .password(passwordEncoder.encode("1234"))
                .uid("john@naver.com")
                .roles(roles)
                .build();
        pctUserRepository.save(pctUserEntity);
        log.info("new user id :" + pctUserEntity.getMsrl());

    }

}