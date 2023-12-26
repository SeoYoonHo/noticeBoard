package com.study.boardExample.swagger;

import io.swagger.v3.oas.models.Operation;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.util.HashMap;
import java.util.Map;

@Component
public class CustomizedOperationCustomizer implements OperationCustomizer {
    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        CustomizedOperation annotaion = handlerMethod.getMethodAnnotation(CustomizedOperation.class);

        if(annotaion != null){
            operation.description(operation.getDescription() + ", " + annotaion.addtion());
            Map<String, Object> map = new HashMap<>();
            map.put("addtion", annotaion.addtion());
            map.put("position", annotaion.position());
            map.put("length", annotaion.length());
            operation.setExtensions(map);
        }

        return operation;
    }
}
