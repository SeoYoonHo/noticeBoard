package com.study.boardExample.shinhan.qryfile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface QryFileFieldAnnotation {
    String qryFileCustomField() default "custom field";
}