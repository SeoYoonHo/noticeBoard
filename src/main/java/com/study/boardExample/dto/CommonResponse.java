package com.study.boardExample.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(staticName = "of")
public class CommonResponse<D> {
    private final String resultCode;
    private final String message;
    private final D data;
}