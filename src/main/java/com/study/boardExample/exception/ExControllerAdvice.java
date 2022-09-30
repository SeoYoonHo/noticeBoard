package com.study.boardExample.exception;

import com.study.boardExample.common.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice("com.study.boardExample.controller")//@ControllerAdvice + @ResponseBody
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoSearchException.class)
    public CommonResponse.NoDataResponse noSearchExceptionHandle(Exception e) {
        log.error("[exceptionHandle] ex", e);
        return CommonResponse.NoDataResponse.of("BAD", e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public CommonResponse.NoDataResponse exHandle(Exception e) {
        log.error("[exceptionHandle] ex", e);
        return CommonResponse.NoDataResponse.of("EX", "내부 오류");
    }
}
