package com.study.boardExample.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JsonWebTokenDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
