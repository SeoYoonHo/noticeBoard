package com.study.boardExample.swagger;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomizedProperty implements PropertyCustomizer {
    @Override
    public Schema customize(Schema property, AnnotatedType type) {
        if(property != null) {
//            Map<String, Schema> map = new HashMap<>();
            Map<String, Schema> map = property.getProperties();
            if(map == null){
                map = new HashMap<>();
            }
//            property.set$schema("ddddddddd");
//            property.setJsonSchema(map);
            map.put("dddddddd",new StringSchema());
        }
//        for(Annotation annotation : annotations){
//
//        }
//        for(Annotation annotation : type.getCtxAnnotations()){
//            System.out.println(annotation.toString());
//        }
        return null;
    }
}
