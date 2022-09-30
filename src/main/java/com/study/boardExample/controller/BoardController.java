package com.study.boardExample.controller;

import com.study.boardExample.dto.MemberDTO;
import com.study.boardExample.dto.CommonResponse;
import com.study.boardExample.exception.ErrorResult;
import com.study.boardExample.exception.NoSearchException;
import com.study.boardExample.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class BoardController {

    private final MemberService memberService;

    @GetMapping("api/v1/search/member/{id}")
    public CommonResponse<MemberDTO.MemberResponse> getMemberById(@PathVariable(value = "id") Long id) {
        MemberDTO.MemberResponse memberResponses = memberService.findMemberById(id);
        return CommonResponse.of("001", "Success", memberResponses);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoSearchException.class)
    public ErrorResult noSearchExceptionHandle(Exception e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }
}
