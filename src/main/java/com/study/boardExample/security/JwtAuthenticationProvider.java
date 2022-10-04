package com.study.boardExample.security;

import com.study.boardExample.exception.JwtInvalidException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@Slf4j
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final String KEY_ROLES = "roles";
    private final byte[] secretKeyByte;

    public JwtAuthenticationProvider(@Value("${spring.jwt.secret}") String secretKey) {
        this.secretKeyByte = secretKey.getBytes();
    }

    private Collection<? extends GrantedAuthority> createGrantedAuthorities(Claims claims) {
        List<String> roles = (List<String>) claims.get(KEY_ROLES);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String role : roles) {
            grantedAuthorities.add(() -> role);
        }
        return grantedAuthorities;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Claims claims;
//        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        Key key = Keys.hmacShaKeyFor(secretKeyByte);

        try {
            claims = Jwts.parserBuilder().setSigningKey(key).build()
                         .parseClaimsJws(((JwtAuthenticationToken) authentication).getJsonWebToken()).getBody();
        } catch (UnsupportedJwtException | IllegalArgumentException unsupportedJwtException) {
            throw new JwtInvalidException("using illegal argument like null", unsupportedJwtException);
        } catch (ExpiredJwtException expiredJwtException) {
            throw new JwtInvalidException("expired token", expiredJwtException);
        } catch (MalformedJwtException malformedJwtException) {
            throw new JwtInvalidException("malformed token", malformedJwtException);
        } catch (SignatureException signatureException) {
            throw new JwtInvalidException("signature key is different", signatureException);
        }
        return new JwtAuthenticationToken(claims.getSubject(), "", createGrantedAuthorities(claims));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}