package com.org.bithumb.apiserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.bithumb.apiserver.domain.entity.PctUserEntity;
import com.org.bithumb.apiserver.domain.repository.PctUserRepository;
import com.org.bithumb.apiserver.security.JwtTokenProvider;
import com.org.bithumb.apiserver.util.ERR_CODE;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Slf4j
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    PctUserRepository pctUserRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @After
    public void resetRepository() throws Exception {
        pctUserRepository.deleteAll();
    }
    @Test
    public void createUserSuccessTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = new HashMap<>();
        map.put("uid", "shson@naver.com");
        map.put("name", "shson");
        map.put("password", "passwd1234!@#$");
        String jsonStr = mapper.writeValueAsString(map);
        log.info(jsonStr);

        //성공시 status : 200, code : 200, message와 result필드가 있음
        mockMvc.perform(post("/v1/member/join")
                .content(jsonStr)
                .contentType(MediaType.APPLICATION_JSON))
                //결
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("code").value(ERR_CODE.OK.getErrCode()))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("result").exists())
                .andDo(print());
    }

    @Test
    public void createUserMissingParamTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = new HashMap<>();
        map.put("uid", "shson@naver.com");
        map.put("password", "12345");
        String jsonStr = mapper.writeValueAsString(map);
        log.info(jsonStr);

        //이름이 빠진 경우 status : 400, code : -30004, message와 result필드가 있음
        mockMvc.perform(post("/v1/member/join")
                .content(jsonStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("code").value(ERR_CODE.MISSING_PARAMETER.getErrCode()))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("result").exists())
                .andDo(print());
    }

    @Test
    public void createUserWeekPasswordTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = new HashMap<>();
        map.put("uid", "shson@naver.com");
        map.put("name", "shson");
        map.put("password", "12345");
        String jsonStr = mapper.writeValueAsString(map);
        log.info(jsonStr);

        //약한패스워드 status : 400, code : -30001, message와 result필드가 있음
        mockMvc.perform(post("/v1/member/join")
                .content(jsonStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("code").value(ERR_CODE.WEEK_PASSWORD.getErrCode()))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("result").exists())
                .andDo(print());
    }

    @Test
    public void createUserInvalidIdTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = new HashMap<>();
        map.put("uid", "shson.co.kr");
        map.put("name", "shson");
        map.put("password", "12345");
        String jsonStr = mapper.writeValueAsString(map);
        log.info(jsonStr);

        //email형식의 id가 아닌경우 status : 400, code : -30003, message와 result필드가 있음
        mockMvc.perform(post("/v1/member/join")
                .content(jsonStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("code").value(ERR_CODE.INVALID_EMAIL_FORMAT.getErrCode()))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("result").exists())
                .andDo(print());
    }

    @Test
    public void loginUserSucessTest() throws Exception {
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_ADMIN");
        PctUserEntity pctUserEntity = PctUserEntity.builder()
                .name("shson")
                .password(passwordEncoder.encode("password1234!@#$"))
                .uid("shson@naver.com")
                .roles(roles)
                .build();
        pctUserRepository.save(pctUserEntity);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = new HashMap<>();
        map.put("uid", "shson@naver.com");
        map.put("password", "password1234!@#$");
        String jsonStr = mapper.writeValueAsString(map);
        log.info(jsonStr);

        //로그인 성공 status : 200, code : 0, message와 result필드가 있음
        MvcResult result = mockMvc.perform(post("/v1/member/login")
                .content(jsonStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(ERR_CODE.OK.getErrCode()))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("result").exists())
                .andDo(print())
                .andReturn();

        //토큰 검증
        String jsonString = result.getResponse().getContentAsString();
        JSONObject jObject = new JSONObject(jsonString);
        String token = jObject.getString("result");
        String user = jwtTokenProvider.getUserPk(token);
        assertTrue(user.equals("shson@naver.com"));
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    public void loginUserMissMatchInfoTest() throws Exception {
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_ADMIN");
        PctUserEntity pctUserEntity = PctUserEntity.builder()
                .name("shson")
                .password(passwordEncoder.encode("password1234!@#$"))
                .uid("shson@naver.com")
                .roles(roles)
                .build();
        pctUserRepository.save(pctUserEntity);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = new HashMap<>();
        map.put("uid", "shson@naver.com");
        map.put("password", "invalidpassword1@");
        String jsonStr = mapper.writeValueAsString(map);
        log.info(jsonStr);

        //비밀번호가 안맞는 경우 status : 400, code : -30009, message와 result필드가 있음
        mockMvc.perform(post("/v1/member/login")
                .content(jsonStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("code").value(ERR_CODE.INVALID_PASSWORD.getErrCode()))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("result").exists())
                .andDo(print());

        //없는 유저인 경우 status : 400, code : -30008, message와 result필드가 있음
        map.put("uid", "none@naver.com");
        map.put("password", "invalidpassword1@");
        jsonStr = mapper.writeValueAsString(map);
        log.info(jsonStr);

        mockMvc.perform(post("/v1/member/login")
                .content(jsonStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("code").value(ERR_CODE.NOT_FOUND_USER.getErrCode()))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("result").exists())
                .andDo(print());
    }

    @Test
    public void userInfoSucessTest() throws Exception {
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_ADMIN");
        PctUserEntity pctUserEntity = PctUserEntity.builder()
                .name("shson")
                .password(passwordEncoder.encode("password1234!@#$"))
                .uid("shson@naver.com")
                .roles(roles)
                .lastLoginTime(LocalDateTime.now())
                .build();
        pctUserRepository.save(pctUserEntity);

        String token = jwtTokenProvider.createToken("shson@naver.com",roles);

        MvcResult result = mockMvc.perform(get("/v1/member/info")
                .header("X-AUTH-TOKEN", token))
                //결과
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(ERR_CODE.OK.getErrCode()))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("result").exists())
                .andDo(print())
                .andReturn();

        //user info 검증
        String jsonString = result.getResponse().getContentAsString();
        JSONObject jObject = new JSONObject(jsonString).getJSONObject("result");
        String uid = jObject.getString("uid");
        String name = jObject.getString("name");
        String lastLogingTime = jObject.getString("lastLoginTime");

        assertTrue(uid.equals("shson@naver.com"));
        assertTrue(name.equals("shson"));
        assertNotNull(lastLogingTime);
    }

    @Test
    public void userInfoAuthFailTest() throws Exception {
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_ADMIN");
        PctUserEntity pctUserEntity = PctUserEntity.builder()
                .name("shson")
                .password(passwordEncoder.encode("password1234!@#$"))
                .uid("shson@naver.com")
                .roles(roles)
                .lastLoginTime(LocalDateTime.now())
                .build();
        pctUserRepository.save(pctUserEntity);
        //인증받지 않은 사용자가 요청하는 경우 status : 401, code : -30005, message와 result필드가 있음
        mockMvc.perform(get("/v1/member/info"))
                //토큰이 없이 요청
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("code").value(ERR_CODE.NOT_AUTHONTICATION.getErrCode()))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("result").exists())
                .andDo(print());
    }

}