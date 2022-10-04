package com.study.boardExample.dto;

import com.study.boardExample.domain.Post;
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
    public static class LoginRequest {
        private String email;
        private String password;
    }

}
