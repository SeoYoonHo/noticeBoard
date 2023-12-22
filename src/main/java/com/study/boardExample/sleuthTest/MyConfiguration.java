package com.study.boardExample.sleuthTest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration(proxyBeanMethods = false)
class MyConfiguration {
    @Bean
    RestTemplate myRestTemplate() {
        return new RestTemplate();
    }
}