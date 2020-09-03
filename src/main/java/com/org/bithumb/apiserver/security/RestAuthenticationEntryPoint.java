package com.org.bithumb.apiserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.bithumb.apiserver.aop.RestResponse;
import com.org.bithumb.apiserver.util.ERR_CODE;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        RestResponse response = new RestResponse(ERR_CODE.NOT_AUTHONTICATION.getErrCode(), "Unauthorised","");
        response.setMessage("Unauthorised");
        OutputStream out = httpServletResponse.getOutputStream();
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, response);
        out.flush();
    }
}
