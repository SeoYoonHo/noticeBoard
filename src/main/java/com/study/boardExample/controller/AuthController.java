package com.study.boardExample.controller;

import com.study.boardExample.dto.JsonWebTokenDto;
import com.study.boardExample.dto.MemberDTO;
import com.study.boardExample.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Tag(name = "로그인 API", description = "로그인용 API")
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final String AUTHORIZATION_HEADER = "Authorization";

    private final AuthService authService;

    private final RestTemplate restTemplate;

    @PostMapping("/login")
    public JsonWebTokenDto login(@RequestBody MemberDTO.LoginRequest memberDto) {
        String URL = "http://localhost:9090/api/v1/auth/test";
        restTemplate.getForObject(URL, String.class);
        return authService.login(memberDto);
    }

    @Operation(summary = "문자열 반복", description = "파라미터로 받은 문자열을 2번 반복합니다.")
//    @Parameter(name = "str", description = "2번 반복할 문자열")
    @GetMapping("/test")
    public String test(){
        log.info("test!!!!!!!!!");
        return null;
    }

    @PostMapping("/reissue")
    public JsonWebTokenDto reissue(@RequestHeader(AUTHORIZATION_HEADER) String bearerToken) {
        return authService.reissue(bearerToken);
    }
}