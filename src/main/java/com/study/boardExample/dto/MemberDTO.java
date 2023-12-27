package com.study.boardExample.dto;

import com.study.boardExample.domain.Post;
import com.study.boardExample.shinhan.qryfile.QryFileFieldAnnotation;
import com.study.boardExample.shinhan.qryfile.QryFileMethodAnnotation;
import com.study.boardExample.shinhan.swagger.CustomizedField;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

public class MemberDTO {

    @Data
    public static class MemberResponse {
        private Long id;
        private String email;
        private String name;
        private Integer age;
        private String gender;
        private String level;
        private String tel;
        private Date createDt;
        private Date lastLoginDt;
        private List<Post> postList;
    }

    @Data
    public static class CreateMemberRequest {
        private String name;
        private Integer age;
        private String gender;
        private String level;
        private String tel;
    }

    @Data
    public static class UpdateMemberRequest {
        private Long id;
        private String name;
        private Integer age;
        private String gender;
        private String level;
        private String tel;
    }

    @Data
    @AllArgsConstructor(staticName = "of")
    public static class LoginRequest {
        @CustomizedField(customField = "sadgadg")
        @QryFileFieldAnnotation(qryFileCustomField = "qryFile custom field!!!")
        private String email;
        @CustomizedField(customField = "hrehwrt")
        private String password;
    }

}
