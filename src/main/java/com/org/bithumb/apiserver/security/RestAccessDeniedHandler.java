package com.org.bithumb.apiserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.bithumb.apiserver.aop.RestResponse;
import com.org.bithumb.apiserver.util.ERR_CODE;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        RestResponse response = new RestResponse(ERR_CODE.NO_PERMISSION_USER.getErrCode(), "Access Denied","");
        response.setMessage("Access Denied");
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        OutputStream out = httpServletResponse.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, response);
        out.flush();
    }
}
