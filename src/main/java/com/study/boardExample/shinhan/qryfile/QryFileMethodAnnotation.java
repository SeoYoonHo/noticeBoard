package com.study.boardExample.shinhan.qryfile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//@Retention(RetentionPolicy.RUNTIME)
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface QryFileMethodAnnotation {
    String qryFileCustomMethod() default "custom method";
}
