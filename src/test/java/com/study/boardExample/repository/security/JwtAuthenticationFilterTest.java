package com.study.boardExample.repository.security;

import com.study.boardExample.exception.JwtInvalidException;
import com.study.boardExample.security.JwtAuthenticationFilter;
import com.study.boardExample.security.JwtAuthenticationToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {
    MockHttpServletRequest mockHttpServletRequest;
    MockHttpServletResponse mockHttpServletResponse;
    @Mock
    FilterChain mockFilterChain;
    @Mock
    AuthenticationManager mockAuthenticationManager;
    @InjectMocks
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    public void setup() {
        mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletResponse = new MockHttpServletResponse();
    }

    @Test
    public void givenTokenNotInHeader_whenDoFilterInternal_thenAuthenticationManagerNotBeenCalled() throws ServletException, IOException {
        //given

        //when
        jwtAuthenticationFilter.doFilterInternal(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);

        //then
        verify(mockAuthenticationManager, never()).authenticate(any());
        verify(mockFilterChain, times(1)).doFilter(mockHttpServletRequest, mockHttpServletResponse);
    }

    @Test
    public void givenInvalidTokenInHeader_whenDoFilterInternal_thenAuthenticationManagerNotBeenCalled() throws ServletException, IOException {
        //given
        mockHttpServletRequest.addHeader("Authorizaion", "invalid token");

        //when
        jwtAuthenticationFilter.doFilterInternal(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);

        //then
        verify(mockAuthenticationManager, never()).authenticate(any());
        verify(mockFilterChain, times(1)).doFilter(mockHttpServletRequest, mockHttpServletResponse);
    }

    @Test
    public void givenReturnNullAfterAuthenticateWithValidToken_whenDoFilterInternal_thenAuthenticationFromSecurityContextHolderIsNull() throws ServletException, IOException {
        //given
        mockHttpServletRequest.addHeader("Authorization", "Bearer valid_token");
        JwtAuthenticationToken token = new JwtAuthenticationToken("valid_token");

        when(mockAuthenticationManager.authenticate(token)).thenReturn(null);

        //when
        jwtAuthenticationFilter.doFilterInternal(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);

        //then
        assertThat(SecurityContextHolder.getContext().getAuthentication(), nullValue());
    }

    @Test
    public void givenThrowAuthenticationException_whenDoFilterInternal_thenSecurityContextInContextHolderIsNullAndClearContextBeenCalled() throws ServletException, IOException {
        //given
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        try (MockedStatic<SecurityContextHolder> utilties = Mockito.mockStatic(SecurityContextHolder.class)) {
            utilties.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            mockHttpServletRequest.addHeader("Authorization", "Bearer valid_token");
            JwtAuthenticationToken token = new JwtAuthenticationToken("valid_token");

            when(mockAuthenticationManager.authenticate(token)).thenThrow(new JwtInvalidException("time expired"));

            //when
            jwtAuthenticationFilter.doFilterInternal(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);

            //then
            utilties.verify(SecurityContextHolder::clearContext, times(1));
            assertThat(SecurityContextHolder.getContext().getAuthentication(), nullValue());
        }
    }

    @Test
    public void givenValidToken_whenDoFilterInternal_thenSecurityContextHasAuthentication() throws ServletException, IOException {
        //given
        mockHttpServletRequest.addHeader("Authorization", "Bearer valid_token");
        JwtAuthenticationToken token = new JwtAuthenticationToken("valid_token");
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(
                "SeoYoonho",
                "",
                Collections.singletonList(() -> "ROLE_ADMIN")
        );

        when(mockAuthenticationManager.authenticate(token)).thenReturn(authenticationToken);

        //when
        jwtAuthenticationFilter.doFilterInternal(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);

        //then
        assertThat(SecurityContextHolder.getContext().getAuthentication(), equalTo(authenticationToken));

    }
}