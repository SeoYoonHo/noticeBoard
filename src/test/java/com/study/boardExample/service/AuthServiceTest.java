package com.study.boardExample.service;

import com.study.boardExample.domain.Member;
import com.study.boardExample.dto.JsonWebTokenDto;
import com.study.boardExample.dto.MemberDTO;
import com.study.boardExample.exception.JwtInvalidException;
import com.study.boardExample.exception.NoSearchException;
import com.study.boardExample.repository.MemberRepository;
import com.study.boardExample.utils.JsonWebTokenIssuer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    MemberRepository memberRepository;
    @Mock
    JsonWebTokenIssuer jsonWebTokenIssuer;
    PasswordEncoder passwordEncoder;

    AuthService authService;

    @BeforeEach
    public void setup() {
        passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        authService = new AuthService(memberRepository, passwordEncoder, jsonWebTokenIssuer);
    }


    @Test
    public void givenNotExistEmail_whenLogin_thenThrowNoSearchException() {
        //given
        MemberDTO.LoginRequest memberDto = MemberDTO.LoginRequest.of("seoyoonho", "1234");

        //when
        Throwable throwable = assertThrows(NoSearchException.class, () -> authService.login(memberDto));

        //then
        assertThat(throwable, isA(NoSearchException.class));
        assertThat(throwable.getMessage(), equalTo("member email is not found"));
    }

    @Test
    public void givenNoMatchedPassword_whenLogin_thenBadCredentialException() {
        //given
        MemberDTO.LoginRequest memberDto = MemberDTO.LoginRequest.of("seoyoonho", "1234");
        Member member = new Member();
        member.setEmail("seoyoonho");
        member.setPassword(passwordEncoder.encode("12345"));
        member.setAuthority("ROLE_ADMIN");


        when(memberRepository.findMemberByEmail("seoyoonho")).thenReturn(Optional.of(member));

        //when
        Throwable throwable = assertThrows(BadCredentialsException.class, () -> authService.login(memberDto));

        //then
        assertThat(throwable, isA(BadCredentialsException.class));
        assertThat(throwable.getMessage(), equalTo("bad credential: using unmatched password"));
    }

    @Test
    public void givenValidMemberDto_whenLogin_thenReturnJsonWebTokenDto() {
        //given
        MemberDTO.LoginRequest memberDto = MemberDTO.LoginRequest.of("seoyoonho", "1234");
        Member member = new Member();
        member.setEmail("seoyoonho");
        member.setPassword(passwordEncoder.encode("1234"));
        member.setAuthority("ROLE_ADMIN");

        when(memberRepository.findMemberByEmail("seoyoonho")).thenReturn(Optional.of(member));
        when(jsonWebTokenIssuer.createAccessToken("seoyoonho", "ROLE_ADMIN")).thenReturn("accessToken");
        when(jsonWebTokenIssuer.createRefreshToken("seoyoonho", "ROLE_ADMIN")).thenReturn("refreshToken");

        //when
        JsonWebTokenDto jsonWebTokenDto = authService.login(memberDto);

        //then
        assertThat(jsonWebTokenDto.getGrantType(), equalTo("Bearer"));
        assertThat(jsonWebTokenDto.getAccessToken(), equalTo("accessToken"));
        assertThat(jsonWebTokenDto.getRefreshToken(), equalTo("refreshToken"));
    }

    @Test
    public void givenInvalidGrandType_whenReissue_thenThrowJwtInvalidException() {
        Throwable throwable = assertThrows(JwtInvalidException.class, () -> authService.reissue("refreshToken"));

        assertThat(throwable, isA(JwtInvalidException.class));
        assertThat(throwable.getMessage(), equalTo("invalid grant type"));
    }

    @Test
    public void givenNullClaims_whenReissue_thenThrowJwtInvalidException() {
        when(jsonWebTokenIssuer.parseClaimsFromRefreshToken("refreshToken")).thenReturn(null);

        Throwable throwable = assertThrows(JwtInvalidException.class, () -> authService.reissue("Bearer refreshToken"));

        assertThat(throwable.getMessage(), equalTo("not exists claims in token"));
    }

    @Test
    public void givenValidRefreshToken_whenReissue_thenJsonWebTokenDto() {
        //given
        Member member = new Member();
        member.setEmail("seoyoonho");
        member.setPassword(passwordEncoder.encode("12345"));
        member.setAuthority("ROLE_ADMIN");

        Claims claims = Jwts.claims().setSubject("seoyoonho");
        claims.put("roles", Collections.singleton("ROLD_ADMIN"));

        when(memberRepository.findMemberByEmail("seoyoonho")).thenReturn(Optional.of(member));
        when(jsonWebTokenIssuer.parseClaimsFromRefreshToken("refreshToken")).thenReturn(claims);
        when(jsonWebTokenIssuer.createAccessToken("seoyoonho", "ROLE_ADMIN")).thenReturn("accessToken");
        when(jsonWebTokenIssuer.createRefreshToken("seoyoonho", "ROLE_ADMIN")).thenReturn("refreshToken");

        //when
        JsonWebTokenDto jsonWebTokenDto = authService.reissue("Bearer refreshToken");

        //then
        assertThat(jsonWebTokenDto.getGrantType(), equalTo("Bearer"));
        assertThat(jsonWebTokenDto.getAccessToken(), equalTo("accessToken"));
        assertThat(jsonWebTokenDto.getRefreshToken(), equalTo("refreshToken"));
    }
}