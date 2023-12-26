package com.study.boardExample.swagger;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.util.*;

@Component
public class SwaggerCustomizer implements OperationCustomizer, PropertyCustomizer {
    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        CustomizedOperation annotaion = handlerMethod.getMethodAnnotation(CustomizedOperation.class);

        if (annotaion != null) {
            operation.description(operation.getDescription() + ", " + annotaion.addtion());
            Map<String, Object> map = new HashMap<>();
            map.put("addtion", annotaion.addtion());
            map.put("position", annotaion.position());
            map.put("length", annotaion.length());
            operation.setExtensions(map);
        }

        return operation;
    }

    @Override
    public Schema customize(Schema property, AnnotatedType type) {
        Annotation[] ctxAnnotations = type.getCtxAnnotations();
        if (ctxAnnotations == null) {
            return property;
        }

        CustomizedField annotation =
                Arrays.stream(ctxAnnotations)
                      .filter(annotation1 -> annotation1.annotationType()
                                                        .equals(CustomizedField.class))
                      .findFirst()
                      .map(annotation1 -> (CustomizedField) annotation1)
                      .orElse(null);

        if (annotation != null) {
            property.addExtension("x-customField", annotation.customField());
            System.out.println(property);
            System.out.println(property.getExtensions());
        }
        return property;
    }
}
