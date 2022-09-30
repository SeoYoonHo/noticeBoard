package com.study.boardExample.mapper;

import com.study.boardExample.domain.Member;
import com.study.boardExample.dto.MemberDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MemberMapper {
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    MemberDTO.MemberResponse memeberToMemberResponseDto(Member memeber);
}
