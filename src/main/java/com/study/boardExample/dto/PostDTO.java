package com.study.boardExample.dto;

import lombok.Data;

public class PostDTO {
    @Data
    public static class PostResponse {
        private Long id;
        private String contents;
        private Integer cnt;
    }

    @Data
    public static class CreatePostRequest {
        private String contents;
    }
}
