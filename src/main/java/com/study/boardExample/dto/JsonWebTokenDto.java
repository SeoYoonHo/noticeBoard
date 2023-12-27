package com.study.boardExample.dto;

import com.study.boardExample.shinhan.qryfile.QryFileOutputFieldAnnotation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JsonWebTokenDto {
    @QryFileOutputFieldAnnotation(qryFileCustomField = "output custom!!!")
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
