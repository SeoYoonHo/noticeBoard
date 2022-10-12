package com.study.boardExample.utils;

import com.study.boardExample.exception.JwtInvalidException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("unchecked")
class JsonWebTokenIssuerTest {

    JsonWebTokenIssuer jsonWebTokenIssuer;

    @BeforeEach
    public void setUp() {
        jsonWebTokenIssuer = new JsonWebTokenIssuer(
                "secretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKey",
                "refreshSecretKeyrefreshSecretKeyrefreshSecretKeyrefreshSecretKeyrefreshSecretKeyrefreshSecretKey",
                10,
                30);
    }

    Claims parseClaims(String jwt, String secretKey) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

        return Jwts.parserBuilder().setSigningKey(key).build()
                   .parseClaimsJws(jwt).getBody();
    }

    @Test
    public void givenUser_whenCreateAccessTokenByUser_thenParsedClaimsWithSameValue() {
        String jwt = jsonWebTokenIssuer.createAccessToken("gogoy2643@naver.com", "ROLE_ADMIN");

        Claims claims = parseClaims(jwt,
                "secretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKey");

        assertThat(claims.getSubject(), equalTo("gogoy2643@naver.com"));
        assertThat(claims.get("roles"), isA(List.class));
        List<String> roles = (List<String>) claims.get("roles");
        for (String role : roles) {
            assertThat(role, equalTo("ROLE_ADMIN"));
        }
    }

    @Test
    public void givenUser_whenCreateRefreshTokenByUser_thenParsedClaimsWithSameValue() {
        String jwt = jsonWebTokenIssuer.createRefreshToken("gogoy2643@naver.com", "ROLE_ADMIN");

        Claims claims = parseClaims(jwt,
                "refreshSecretKeyrefreshSecretKeyrefreshSecretKeyrefreshSecretKeyrefreshSecretKeyrefreshSecretKey");

        assertThat(claims.getSubject(), equalTo("gogoy2643@naver.com"));
        assertThat(claims.get("roles"), isA(List.class));
        List<String> roles = (List<String>) claims.get("roles");
        for (String role : roles) {
            assertThat(role, equalTo("ROLE_ADMIN"));
        }
    }

    @Test
    public void givenInvalidRefreshToken_whenParseClaimsFromRefreshToken_thenThrowJwtInvalidException() {
        String invalidRefreshToken = "invalid refresh token";

        assertThrows(JwtInvalidException.class,
                () -> jsonWebTokenIssuer.parseClaimsFromRefreshToken(invalidRefreshToken));
    }

    @Test
    public void givenAccessToken_whenParseClaimsFromRefreshToken_thenThrowsJwtInvalidException() {
        String accessToken = jsonWebTokenIssuer.createAccessToken("gogoy2643@naver.com", "ROLE_ADMIN");

        assertThrows(JwtInvalidException.class, () -> jsonWebTokenIssuer.parseClaimsFromRefreshToken(accessToken));
    }

    @Test
    public void givenRefreshToken_whenParseClaimsFromRefreshToken_thenReturnClaims() {
        String refreshToken = jsonWebTokenIssuer.createRefreshToken("gogoy2643@naver.com", "ROLE_ADMIN");

        Claims claims = jsonWebTokenIssuer.parseClaimsFromRefreshToken(refreshToken);

        assertThat(claims.getSubject(), equalTo("gogoy2643@naver.com"));
        assertThat(claims.get("roles"), isA(List.class));

        List<String> roles = (List<String>) claims.get("roles");
        for (String role : roles) {
            assertThat(role, equalTo("ROLE_ADMIN"));
        }
    }
}