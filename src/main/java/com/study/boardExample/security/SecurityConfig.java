package com.study.boardExample.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.boardExample.common.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Slf4j
public class SecurityConfig {

    private final String ROLE_ADMIN = "ADMIN";
    private final String ROLE_NORMAL = "NORMAL";

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public SecurityConfig(
            AuthenticationManagerBuilder authenticationManagerBuilder,
            JwtAuthenticationProvider jsonWebTokenProvider
    ) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.authenticationManagerBuilder.authenticationProvider(jsonWebTokenProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // REST API 방식이므로 CSRF 보안 토큰 생성 기능 종료
                .csrf().disable()
                // 요청 별 인증 필요 여부 혹은 권한 확인
                .authorizeRequests()
                // /auth 로 시작하는 모든 경로는 권한 확인 없이 수행 가능합니다.
                .antMatchers("/api/v1/auth/**","/h2-console/**").permitAll()
                // 나머지는 인증 확인 및 역할 확인
                .anyRequest()
                .hasAnyRole(ROLE_ADMIN, ROLE_NORMAL)
                // h2-console 사용을 위한 설정
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, accessDeniedException) -> {
                            ObjectMapper mapper = new ObjectMapper();

                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setCharacterEncoding("UTF-8");
                            CommonResponse.NoDataResponse obejct = CommonResponse.NoDataResponse.of("unauthorized", accessDeniedException.getMessage());
                            mapper.writeValue(response.getWriter(), obejct);
                            SecurityContextHolder.clearContext();
                        })
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()
                // 세션을 사용하지 않도록 변경
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // JWT 토큰 인증 필터 설정
                .and()
                .apply(new JwtSecurityConfig(authenticationManagerBuilder.getOrBuild()));

        return http.build();
    }
}