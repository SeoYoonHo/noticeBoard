package com.study.boardExample.service;

import com.study.boardExample.domain.Member;
import com.study.boardExample.dto.MemberDTO;
import com.study.boardExample.exception.NoSearchException;
import com.study.boardExample.mapper.MemberMapper;
import com.study.boardExample.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository userRepository;

    public MemberDTO.MemberResponse findMemberById(Long id) {
        Member member = userRepository.findById(id).orElseThrow(() -> new NoSearchException("No search member"));
        return MemberMapper.INSTANCE.memeberToMemberResponseDto(member);
    }
}
