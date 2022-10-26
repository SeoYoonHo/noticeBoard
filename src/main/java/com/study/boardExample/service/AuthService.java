package com.study.boardExample.service;

import com.study.boardExample.domain.Member;
import com.study.boardExample.dto.JsonWebTokenDto;
import com.study.boardExample.dto.MemberDTO;
import com.study.boardExample.exception.JwtInvalidException;
import com.study.boardExample.exception.NoSearchException;
import com.study.boardExample.repository.MemberRepository;
import com.study.boardExample.utils.JsonWebTokenIssuer;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final String GRANT_TYPE_BEARER = "Bearer";

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JsonWebTokenIssuer jwtIssuer;

    private JsonWebTokenDto createJsonWebTokenDto(Member member) {
        String email = member.getEmail();
        String authority = member.getAuthority();
        return JsonWebTokenDto.builder()
                              .grantType(GRANT_TYPE_BEARER)
                              .accessToken(jwtIssuer.createAccessToken(email, authority))
                              .refreshToken(jwtIssuer.createRefreshToken(email, authority))
                              .build();
    }

    public JsonWebTokenDto login(MemberDTO.LoginRequest memberDto) {

        Member member = memberRepository.findMemberByEmail(memberDto.getEmail())
                                        .orElseThrow(() -> new NoSearchException("member email is not found"));

        if (!passwordEncoder.matches(memberDto.getPassword(), member.getPassword())) {
            throw new BadCredentialsException("bad credential: using unmatched password");
        }

        return createJsonWebTokenDto(member);
    }

    public JsonWebTokenDto reissue(String bearerToken) {
        String refreshToken = jwtIssuer.resolveToken(bearerToken);
        if (!StringUtils.hasText(refreshToken)) {
            throw new JwtInvalidException("invalid grant type");
        }

        Claims claims = jwtIssuer.parseClaimsFromRefreshToken(refreshToken);
        if (claims == null) {
            throw new JwtInvalidException("not exists claims in token");
        }

        Member member = memberRepository.findMemberByEmail(claims.getSubject())
                                        .orElseThrow(() -> new NoSearchException("member email is not found"));

        return createJsonWebTokenDto(member);
    }
}