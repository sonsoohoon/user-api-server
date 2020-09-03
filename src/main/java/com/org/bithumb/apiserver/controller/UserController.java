package com.org.bithumb.apiserver.controller;

import com.org.bithumb.apiserver.aop.RestResponse;
import com.org.bithumb.apiserver.dto.PctUserDto;
import com.org.bithumb.apiserver.service.UserService;
import com.org.bithumb.apiserver.util.ERR_CODE;
import com.org.bithumb.apiserver.util.PcaException;
import com.org.bithumb.apiserver.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController // 결과값을 JSON으로 출력합니다.
@Slf4j
@RequestMapping("/v1/member")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/join")
    public RestResponse<Object> createUser(@RequestBody Map<String,String> param) throws Exception {
        String uid = param.get("uid");
        String name = param.get("name");
        String password = param.get("password");
        //파라미터 체크
        if (uid == null || name == null || password == null)
            throw new PcaException("invalid parameter: " + (uid == null ?
                    "omit email" :name == null ?
                    "omit name" : "omit password"), ERR_CODE.MISSING_PARAMETER);
        //이메일 유효성 검증
        if (!Util.isValidEmail(uid)) {
            throw new PcaException("not email format: " + uid, ERR_CODE.INVALID_EMAIL_FORMAT);
        }
        if (!Util.isValidPassword(password)) {
            throw new PcaException("need more strong password", ERR_CODE.WEEK_PASSWORD);
        }
        userService.signUp(uid, name, password);
        return new RestResponse<>(HttpStatus.OK.value(), "success", "sucees create user");
    }

    @PostMapping(value = "/login")
    public RestResponse<Object> login(@RequestBody Map<String,String> param) throws Exception {
        String uid = param.get("uid");
        String password = param.get("password");
        //파라미터 체크
        if (uid == null || password == null)
            throw new PcaException("invalid parameter: " + (uid == null ?
                    "omit email" : "omit password"),ERR_CODE.MISSING_PARAMETER);

        String token = userService.signIn(uid, password);
        return new RestResponse<>(HttpStatus.OK.value(), "success", token);
    }

    @GetMapping(value = "/info")
    public RestResponse<Object> info() throws Exception {
        //정상 로그인 되었는지 한번더 체크
        String uid = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .orElseThrow(() -> new PcaException("not authenticatied user", ERR_CODE.NOT_AUTHONTICATION))
                .getName();
        PctUserDto userInfo = userService.info(uid);
        return new RestResponse<>(HttpStatus.OK.value(), "success", userInfo);
    }

    @GetMapping(value = "/error")
    public RestResponse<Object> filterError(@RequestParam Map<String, String> param) throws Exception {
        return new RestResponse<>(ERR_CODE.FILTER_ERROR.getErrCode(), param.get("msg"), "");
    }
}
