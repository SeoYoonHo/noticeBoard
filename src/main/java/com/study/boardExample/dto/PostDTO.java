package com.study.boardExample.dto;

import lombok.Data;

public class PostDTO {
    @Data
    public static class PostResponse {
        private Long id;
        private String contents;
        private Integer cnt;
        private String email;
    }

    @Data
    public static class CreatePostRequest {
        private String contents;
    }

    @Data
    public static class UpdatePostRequest {
        private Long id;
        private String contents;
    }

    @Data
    public static class DeletePostRequest {
        private Long id;
    }
}
