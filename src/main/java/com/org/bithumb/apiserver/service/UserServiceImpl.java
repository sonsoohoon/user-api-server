package com.org.bithumb.apiserver.service;

import com.org.bithumb.apiserver.dto.PctUserDto;
import com.org.bithumb.apiserver.security.JwtTokenProvider;
import com.org.bithumb.apiserver.domain.entity.PctUserEntity;
import com.org.bithumb.apiserver.domain.repository.PctUserRepository;
import com.org.bithumb.apiserver.util.ERR_CODE;
import com.org.bithumb.apiserver.util.PcaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private PctUserRepository pctUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @GetMapping(value = "/user")
    public List<PctUserEntity> findAllUser() {
        return pctUserRepository.findAll();
    }

    @Override
    public void signUp(String uid, String name, String password) throws Exception {
        //유저 엔티티 생성 후 저장
        PctUserEntity user = PctUserEntity.builder()
                .uid(uid)
                .name(name)
                .password(passwordEncoder.encode(password))
                .roles(Collections.singletonList("ROLE_ADMIN"))
                .build();
        if (pctUserRepository.findByUid(uid).isPresent())
            throw new PcaException("already exist user: " + uid, ERR_CODE.ALREADY_EXIST_USER);
        pctUserRepository.save(user);
    }

    @Override
    public String signIn(String uid, String password) throws Exception {
            PctUserEntity pctUserEntity = pctUserRepository.findByUid(uid).orElseThrow(() -> new PcaException("not found uid: " + uid, ERR_CODE.NOT_FOUND_USER));
            if (!passwordEncoder.matches(password, pctUserEntity.getPassword()))
                throw new PcaException("invalid password", ERR_CODE.INVALID_PASSWORD);

            pctUserEntity.setLastLoginTime(LocalDateTime.now());
            pctUserRepository.save(pctUserEntity);

        return jwtTokenProvider.createToken(pctUserEntity.getUid(), pctUserEntity.getRoles());
    }

    @Override
    public PctUserDto info(String uid) throws Exception {
        PctUserEntity pctUserEntity = pctUserRepository.findByUid(uid).orElseThrow(() -> new PcaException("not found uid: " + uid, ERR_CODE.NOT_FOUND_USER));
        PctUserDto pctUserDto = PctUserDto.builder()
                .uid(pctUserEntity.getUid())
                .name(pctUserEntity.getName())
                .lastLoginTime(pctUserEntity.getLastLoginTime())
                .build();
        return pctUserDto;
    }
}
