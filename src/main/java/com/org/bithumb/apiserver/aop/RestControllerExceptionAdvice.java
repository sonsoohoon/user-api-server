package com.org.bithumb.apiserver.aop;

import com.org.bithumb.apiserver.util.PcaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
@EnableWebMvc
public class RestControllerExceptionAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ Exception.class })
    public RestResponse handlerRuntimeException(Exception e, HttpServletRequest req){
        log.error("================= Handler RuntimeException =================");
        return new RestResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                ""
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PcaException.class)
    public RestResponse handlerMethodArgumentNotValidException(PcaException e,
                                                                HttpServletRequest req){
        log.error("================= Handler PcaException =================");
        return new RestResponse<>(
                e.getErrCode(),
                e.getMessage(),
                ""
        );
    }
}