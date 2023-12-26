package com.study.boardExample.swagger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CustomizedOperation {
    String addtion() default "customized operation";
    String position() default "";
    String length() default "";
}
