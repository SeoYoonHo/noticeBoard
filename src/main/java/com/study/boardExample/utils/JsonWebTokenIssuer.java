package com.study.boardExample.utils;

import com.study.boardExample.exception.JwtInvalidException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collections;
import java.util.Date;

@Component
public class JsonWebTokenIssuer {

    private final int ONE_SECONDS = 1000;
    private final int ONE_MINUTE = 60 * ONE_SECONDS;
    private final String KEY_ROLES = "roles";

    private final String secretKey;
    private final String refreshSecretKey;
    private final int expireMin;
    private final int refreshExpireMin;

    public JsonWebTokenIssuer(
            @Value("${spring.jwt.secret}") String secretKey,
            @Value("${spring.jwt.refresh-secret}") String refreshSecretKey,
            @Value("${spring.jwt.expire-min:10}") int expireMin,
            @Value("${spring.jwt.refresh-expire-min:30}") int refreshExpireMin) {
        this.secretKey = secretKey;
        this.refreshSecretKey = refreshSecretKey;
        this.expireMin = expireMin;
        this.refreshExpireMin = refreshExpireMin;
    }

    private String createToken(String userName, String authority, String secretKey, int expireMin) {
        Date now = new Date();
        Claims claims = Jwts.claims().setSubject(userName);
        claims.put(KEY_ROLES, Collections.singleton(authority));
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

        return Jwts.builder()
                   .setClaims(claims)
                   .setIssuedAt(now)
                   .setExpiration(new Date(now.getTime() + (long) ONE_MINUTE * expireMin))
                   .signWith(key)
                   .compact();
    }

    public String createAccessToken(String userName, String authority) {
        return createToken(userName, authority, secretKey, expireMin);
    }

    public String createRefreshToken(String userName, String authority) {
        return createToken(userName, authority, refreshSecretKey, refreshExpireMin);
    }

    public Claims parseClaimsFromRefreshToken(String jsonWebToken) {
        Claims claims;
        Key key = Keys.hmacShaKeyFor(refreshSecretKey.getBytes());
        try {
//            claims = Jwts.parser().setSigningKey(refreshSecretKeyBytes).parseClaimsJws(jsonWebToken).getBody();
            claims = Jwts.parserBuilder().setSigningKey(key).build()
                         .parseClaimsJws(jsonWebToken).getBody();
        } catch (UnsupportedJwtException | IllegalArgumentException unsupportedJwtException) {
            throw new JwtInvalidException("using illegal argument like null", unsupportedJwtException);
        } catch (ExpiredJwtException expiredJwtException) {
            throw new JwtInvalidException("expired token", expiredJwtException);
        } catch (MalformedJwtException malformedJwtException) {
            throw new JwtInvalidException("malformed token", malformedJwtException);
        } catch (SignatureException signatureException) {
            throw new JwtInvalidException("signature key is different", signatureException);
        }
        return claims;
    }
}