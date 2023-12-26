package com.study.boardExample.swagger;

import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.ParameterCustomizer;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class SpringdocParameterCustomizer implements ParameterCustomizer {

    @Override
    public Parameter customize(Parameter parameter, MethodParameter methodParameter) {
//        System.out.println("parameter.getName() : " + methodParameter.getParameter().getName());
//        System.out.println("parameterName : " + methodParameter.getParameterName());
//        System.out.println(methodParameter.getAnnotatedElement());
//        System.out.println(methodParameter.getAnnotatedElement().getAnnotation(SpringdocCustomProperty.class));
//        SpringdocCustomProperty customPropertyAnnotation = methodParameter.getParameterAnnotation(SpringdocCustomProperty.class);
//        if (customPropertyAnnotation != null) {
//            System.out.println("실행되냐??");
//            parameter.setName(customPropertyAnnotation.name());
//            parameter.setDescription(customPropertyAnnotation.description());
//            parameter.setRequired(customPropertyAnnotation.required());
//        }

//        Field[] fields = methodParameter.getDeclaringClass().getDeclaredFields();
//        for (Field field : fields) {
//            System.out.println("className : " + field.getDeclaringClass().getName() + ", field : " + field.getName());
//            SpringdocCustomProperty customPropertyAnnotation = field.getAnnotation(SpringdocCustomProperty.class);
//            if (customPropertyAnnotation != null && field.getName().equals(methodParameter.getParameterName())) {
//                System.out.println("실행되냐??");
//                parameter.setName(customPropertyAnnotation.name());
//                parameter.setDescription(customPropertyAnnotation.description());
//                parameter.setRequired(customPropertyAnnotation.required());
//                break;
//            }
//        }
        return parameter;
    }
}
