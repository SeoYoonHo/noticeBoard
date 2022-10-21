package com.study.boardExample.enums;

import com.study.boardExample.exception.InvalidBoardTypeException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum BoardTypeEnums {
    notice(1L, "공지사항"),
    free(2L, "자유게시판"),
    admin(3L, "운영자게시판");

    private final Long code;
    private final String desc;

    public static BoardTypeEnums getBoardType(String typeName) {
        return Arrays.stream(BoardTypeEnums.values())
                     .filter(boardTypeEnums -> boardTypeEnums.toString().equals(typeName))
                     .findAny()
                     .orElseThrow(() -> new InvalidBoardTypeException("Invalid Board Type!!"));
    }
}
