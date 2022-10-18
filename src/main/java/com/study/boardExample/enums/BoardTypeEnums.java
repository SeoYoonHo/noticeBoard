package com.study.boardExample.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BoardTypeEnums {
    notice("notice", "공지사항"),
    free("free", "자유게시판"),
    admin("admin", "관리자게시판");

    private final String code;
    private final String desc;
}
