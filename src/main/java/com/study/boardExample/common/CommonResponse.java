package com.study.boardExample.common;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class CommonResponse {

    @RequiredArgsConstructor(staticName = "of")
    @Data
    public static class DataResponse<D> {
        private final String resultCode;
        private final String message;
        private final D data;
    }

    @RequiredArgsConstructor(staticName = "of")
    @Data
    public static class NoDataResponse {
        private final String resultCode;
        private final String message;
    }

}