package com.study.boardExample.shinhan.qryfile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class QryFileAnnotationReader {

    @Autowired
    private QryFileAnnotationProcessor annotationProcessor;

    @Bean
    public CommandLineRunner initialize() {
        return args -> {
            // 초기화 시 실행할 로직
            annotationProcessor.processAnnotations();
        };
    }
}