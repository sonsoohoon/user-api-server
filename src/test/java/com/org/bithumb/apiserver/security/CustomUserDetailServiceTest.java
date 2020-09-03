package com.org.bithumb.apiserver.security;

import com.org.bithumb.apiserver.domain.entity.PctUserEntity;
import com.org.bithumb.apiserver.domain.repository.PctUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest
public class CustomUserDetailServiceTest {

    @Autowired
    private PctUserRepository pctUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void userRepositoryTests() {
       Optional<PctUserEntity> pctUserEntity = pctUserRepository.findByUid("shson@naver.com");
       log.info(pctUserEntity.toString());
       log.info(passwordEncoder.encode("passwd") + "match: " + passwordEncoder.matches("passwd", passwordEncoder.encode("passwd")));
    }
}