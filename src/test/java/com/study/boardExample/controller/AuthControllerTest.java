package com.study.boardExample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.boardExample.dto.JsonWebTokenDto;
import com.study.boardExample.utils.JsonWebTokenIssuer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.security.Key;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    private final int ONE_SECONDS = 1000;
    private final int ONE_MINUTE = 60 * ONE_SECONDS;
    private final String KEY_ROLES = "roles";

    @Autowired
    MockMvc movkMvc;
    @Autowired
    ObjectMapper objectMapper;

    @SpyBean
    JsonWebTokenIssuer jsonWebTokenIssuer;

    @AfterEach
    public void clear() {
        reset(jsonWebTokenIssuer);
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

    private String getAccessToken() {
        return createToken(
                "gogoy2643@naver.com",
                Collections.singletonList("ROLE_ADMIN"),
                new Date(),
                "validSecretKeyvalidSecretKeyvalidSecretKeyvalidSecretKeyvalidSecretKeyvalidSecretKeyvalidSecretKeyvalidSecretKey",
                10);
    }

    private String getRefreshToken() {
        return createToken(
                "gogoy2643@naver.com",
                Collections.singletonList("ROLE_ADMIN"),
                new Date(),
                "refreshSecretKeyrefreshSecretKeyrefreshSecretKeyrefreshSecretKeyrefreshSecretKeyrefreshSecretKeyrefreshSecretKeyrefreshSecretKey",
                30);
    }

    @Test
    public void givenWithoutToken_whenCallNotExistsPath_thenIsUnauthorized() throws Exception {
        movkMvc.perform(post("/others")).andExpect(status().isUnauthorized());
    }

    @Test
    public void givenInvalidToken_whenCallNotExistsPath_thenIsUnauthorized() throws Exception {
        String inValidToken = createToken("gogody2643@naver.com", Collections.singletonList("ROLE_ADMIN"), new Date(),
                "invalidKeyinvalidKeyinvalidKeyinvalidKey", 30);

        movkMvc.perform(post("/somthing-others").header("Authorization", "Bearer " + inValidToken))
               .andExpect(status().isUnauthorized());
    }

    @Test
    public void givenExpiredToken_whenCallNotExistPath_thenIsUnauthorized() throws Exception {
        Date past = new Date(System.currentTimeMillis() - ONE_MINUTE * 10);
        String expiredToken = createToken("gogody2643@naver.com", Collections.singletonList("ROLE_ADMIN"), past,
                "invalidKeyinvalidKeyinvalidKeyinvalidKey", 30);

        movkMvc.perform(post("/somthing-others").header("Authorization", "Bearer " + expiredToken))
               .andExpect(status().isUnauthorized());
    }

    @Test
    public void givenValidToken_whenCallNotExistsPath_thenNotFound() throws Exception {
        String expiredToken = createToken("gogody2643@naver.com", Collections.singletonList("ROLE_ADMIN"), new Date(),
                "validSecretKeyvalidSecretKeyvalidSecretKeyvalidSecretKeyvalidSecretKeyvalidSecretKeyvalidSecretKeyvalidSecretKey",
                30);

        movkMvc.perform(post("/somthing-others").header("Authorization", "Bearer " + expiredToken))
               .andExpect(status().isNotFound());
    }

    @Test
    public void givenWithoutToken_whenCallLogin_thenIsOk() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("email", "gogoy2643@naver.com");
        paramMap.put("password", "123");

        movkMvc.perform(
                       post("/api/v1/auth/login")
                               .contentType(MediaType.APPLICATION_JSON)
                               .accept(MediaType.APPLICATION_JSON)
                               .content(objectMapper.writeValueAsString(paramMap))
               )
               .andExpect(status().isOk());
    }

    @Test
    public void givenNotMatchedPasswordDto_whenLogin_thenIsUnauthorized() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("email", "gogoy2643@naver.com");
        paramMap.put("password", "12345");

        movkMvc.perform(
                       post("/api/v1/auth/login")
                               .contentType(MediaType.APPLICATION_JSON)
                               .accept(MediaType.APPLICATION_JSON)
                               .content(objectMapper.writeValueAsString(paramMap))
               )
               .andExpect(status().isUnauthorized());
    }

    @Test
    public void givenValidUserDto_whenLogin_thenReturnAccessToken() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String accessToken = getAccessToken();
        String refreshToken = getRefreshToken();
        when(jsonWebTokenIssuer.createAccessToken("gogoy2643@naver.com", "ROLE_ADMIN")).thenReturn(accessToken);
        when(jsonWebTokenIssuer.createRefreshToken("gogoy2643@naver.com", "ROLE_ADMIN")).thenReturn(refreshToken);

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("email", "gogoy2643@naver.com");
        paramMap.put("password", "123");

        MvcResult mvcResult = movkMvc.perform(
                                             post("/api/v1/auth/login")
                                                     .contentType(MediaType.APPLICATION_JSON)
                                                     .accept(MediaType.APPLICATION_JSON)
                                                     .content(objectMapper.writeValueAsString(paramMap))
                                     )
                                     .andExpect(status().isOk())
                                     .andReturn();
        JsonWebTokenDto jsonWebTokenDto = mapper.readValue(mvcResult.getResponse().getContentAsString(),
                JsonWebTokenDto.class);

        assertThat(jsonWebTokenDto.getAccessToken(), equalTo(accessToken));
        assertThat(jsonWebTokenDto.getRefreshToken(), equalTo(refreshToken));
        assertThat(jsonWebTokenDto.getGrantType(), equalTo("Bearer"));
    }

    @Test
    public void givenWithoutAuthorization_whenReissue_thenIsBadRequest() throws Exception {
        movkMvc.perform(post("/api/v1/auth/reissue")).andExpect(status().isBadRequest());
    }

    @Test
    public void givenNotBearerToken_whenReissue_thenIsUnauthorized() throws Exception {
        String refreshToken = getRefreshToken();

        movkMvc.perform(post("/api/v1/auth/reissue").header("Authorization", refreshToken))
               .andExpect(status().isUnauthorized());
    }

    @Test
    public void givenAccessToken_whenReissue_thenIsUnauthorized() throws Exception {
        String accessToekn = getAccessToken();

        movkMvc.perform(post("/api/v1/auth/reissue").header("Authorization", "Bearer " + accessToekn))
               .andExpect(status().isUnauthorized());
    }

    @Test
    public void givenRefreshToken_whenReissue_thenReturnAccessToken() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String accessToken = getAccessToken();
        String refreshToken = getRefreshToken();
        when(jsonWebTokenIssuer.createAccessToken("gogoy2643@naver.com", "ROLE_ADMIN")).thenReturn(accessToken);
        when(jsonWebTokenIssuer.createRefreshToken("gogoy2643@naver.com", "ROLE_ADMIN")).thenReturn(refreshToken);

        MvcResult mvcResult = movkMvc.perform(
                                             post("/api/v1/auth/reissue")
                                                     .header("Authorization", "Bearer " + refreshToken)
                                                     .contentType(MediaType.APPLICATION_JSON)
                                                     .accept(MediaType.APPLICATION_JSON)
                                     )
                                     .andExpect(status().isOk())
                                     .andReturn();
        JsonWebTokenDto jsonWebTokenDto = mapper.readValue(mvcResult.getResponse().getContentAsString(),
                JsonWebTokenDto.class);

        assertThat(jsonWebTokenDto.getAccessToken(), equalTo(accessToken));
        assertThat(jsonWebTokenDto.getRefreshToken(), equalTo(refreshToken));
        assertThat(jsonWebTokenDto.getGrantType(), equalTo("Bearer"));
    }
}