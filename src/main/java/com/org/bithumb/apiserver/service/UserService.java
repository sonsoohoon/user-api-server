package com.org.bithumb.apiserver.service;

import com.org.bithumb.apiserver.dto.PctUserDto;

public interface UserService {
    void signUp(String uid, String name, String password) throws Exception;

    String signIn(String uid, String password) throws Exception;

    PctUserDto info(String uid) throws Exception;
}
