package com.study.boardExample.controller;

import com.study.boardExample.dto.JsonWebTokenDto;
import com.study.boardExample.dto.MemberDTO;
import com.study.boardExample.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final String AUTHORIZATION_HEADER = "Authorization";

    private final AuthService authService;

    @PostMapping("/login")
    public JsonWebTokenDto login(@RequestBody MemberDTO.LoginRequest memberDto) {
        return authService.login(memberDto);
    }

    @PostMapping("/reissue")
    public JsonWebTokenDto reissue(@RequestHeader(AUTHORIZATION_HEADER) String bearerToken) {
        return authService.reissue(bearerToken);
    }
}