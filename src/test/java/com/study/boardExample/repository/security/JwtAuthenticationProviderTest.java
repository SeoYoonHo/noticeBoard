package com.study.boardExample.repository.security;

import com.study.boardExample.exception.JwtInvalidException;
import com.study.boardExample.security.JwtAuthenticationProvider;
import com.study.boardExample.security.JwtAuthenticationToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.security.Key;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtAuthenticationProviderTest {

    final int ONE_SECONDS = 1000;
    final int ONE_MINUTE = 60 * ONE_SECONDS;
    final String KEY_ROLES = "roles";
    JwtAuthenticationProvider provider;

    @BeforeEach
    public void setUp() {
        provider = new JwtAuthenticationProvider(
                "validSecretKeyvalidSecretKeyvalidSecretKeyvalidSecretKeyvalidSecretKey");
    }

    private String createToken(String email, List<String> roles, Date now, String secretKey, int expireMin) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put(KEY_ROLES, roles);
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

        return Jwts.builder()
                   .setClaims(claims)
                   .setIssuedAt(now)
                   .setExpiration(new Date(now.getTime() + (long) ONE_MINUTE * expireMin))
                   .signWith(key)
                   .compact();
    }

    @Test
    public void givenNotSuppertAuthentication_whenCallSupports_thenReturnFalse() {
        assertThat(provider.supports(UsernamePasswordAuthenticationToken.class), equalTo(false));
        assertThat(provider.supports(AbstractAuthenticationToken.class), equalTo(false));
        assertThat(provider.supports(Authentication.class), equalTo(false));
    }

    @Test
    public void givenSupportAuthentication_whenCallSupports_thenReturnTrue() {
        assertThat(provider.supports(JwtAuthenticationToken.class), equalTo(true));
    }

    @Test
    public void givenTokenMadeByDifferentSecretKey_whenCallAuthentication_thenThrowJwtInvalidException() {
        String invalidToken = createToken(
                "gogoy2643@naver.com",
                Collections.singletonList("ROLE_ADMIN"),
                new Date(),
                "invalidSecretKeyinvalidSecretKeyinvalidSecretKeyinvalidSecretKey",
                30);

        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(invalidToken);

        Throwable throwable = assertThrows(JwtInvalidException.class, () -> provider.authenticate(authenticationToken));

        assertThat(throwable, isA(JwtInvalidException.class));
        assertThat(throwable.getMessage(), equalTo("signature key is different"));
    }

    @Test
    public void givenExpiredToken_whenCallAuthentication_thenThrowJwtInvalidException() {

        Date past = new Date(System.currentTimeMillis() - ONE_MINUTE * 10);

        String invalidToken = createToken(
                "Junhyunny",
                Collections.singletonList("ROLE_ADMIN"),
                past,
                "validSecretKeyvalidSecretKeyvalidSecretKeyvalidSecretKeyvalidSecretKey",
                5);
        JwtAuthenticationToken authentication = new JwtAuthenticationToken(invalidToken);

        Throwable throwable = assertThrows(JwtInvalidException.class, () -> provider.authenticate(authentication));

        assertThat(throwable, isA(JwtInvalidException.class));
        assertThat(throwable.getMessage(), equalTo("expired token"));
    }

    @Test
    public void givenMalformedToken_whenCallAuthentication_thenThrowJwtInvalidException() {
        JwtAuthenticationToken authentication = new JwtAuthenticationToken("some malformed token here");

        Throwable throwable = assertThrows(JwtInvalidException.class, () -> provider.authenticate(authentication));

        assertThat(throwable, isA(JwtInvalidException.class));
        assertThat(throwable.getMessage(), equalTo("malformed token"));
    }

    @Test
    public void givenNullJwt_whenCallAuthentication_thenThrowJwtInvalidException() {
        JwtAuthenticationToken authentication = new JwtAuthenticationToken(null);

        Throwable throwable = assertThrows(JwtInvalidException.class, () -> provider.authenticate(authentication));

        assertThat(throwable, isA(JwtInvalidException.class));
        assertThat(throwable.getMessage(), equalTo("using illegal argument like null"));
    }

    @Test
    public void givenValidToken_whenCallAuthentication_thenReturnAuthentication() {
        String validToken = createToken(
                "gogoy2643@naver.com",
                Collections.singletonList("ROLE_ADMIN"),
                new Date(),
                "validSecretKeyvalidSecretKeyvalidSecretKeyvalidSecretKeyvalidSecretKey",
                30);
        JwtAuthenticationToken authentication = new JwtAuthenticationToken(validToken);

        Authentication authenticated = provider.authenticate(authentication);

        assertThat(authenticated.getPrincipal(), equalTo("gogoy2643@naver.com"));
        assertThat(authenticated.getCredentials(), equalTo(""));
        Collection<? extends GrantedAuthority> authorities = authenticated.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            assertThat(authority.getAuthority(), equalTo("ROLE_ADMIN"));
        }
    }
}