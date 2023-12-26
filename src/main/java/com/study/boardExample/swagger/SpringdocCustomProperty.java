package com.study.boardExample.swagger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SpringdocCustomProperty {
    String name() default "";
    String description() default "";
    String type() default "";
    boolean required() default false;
    // 필요한 다른 속성들을 추가할 수 있습니다.
}