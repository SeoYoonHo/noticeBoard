package com.study.boardExample.mapper;

import com.study.boardExample.domain.Member;
import com.study.boardExample.dto.MemberDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    MemberDTO.MemberResponse memeberToMemberResponseDto(Member memeber);

    Member memberRquestDtoToMember(MemberDTO.MemberRequest memberDTO);
}
