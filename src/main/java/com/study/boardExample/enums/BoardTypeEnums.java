package com.study.boardExample.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BoardTypeEnums {
    notice(1L, "notice"),
    free(2L, "free"),
    admin(3L, "admin");

    private final Long code;
    private final String typeName;
}
