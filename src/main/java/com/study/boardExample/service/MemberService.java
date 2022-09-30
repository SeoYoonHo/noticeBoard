package com.study.boardExample.service;

import com.study.boardExample.domain.Member;
import com.study.boardExample.dto.MemberDTO;
import com.study.boardExample.exception.NoSearchException;
import com.study.boardExample.mapper.MemberMapper;
import com.study.boardExample.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository userRepository;

    public MemberDTO.MemberResponse findMemberById(Long id) {
        Member member = userRepository.findById(id).orElseThrow(() -> new NoSearchException("No search member"));
        return MemberMapper.INSTANCE.memeberToMemberResponseDto(member);
    }

    public List<MemberDTO.MemberResponse> findMemberByName(String name) {
        List<Member> memberList = userRepository.findByName(name);
        return memberList.stream()
                         .map(MemberMapper.INSTANCE::memeberToMemberResponseDto).collect(Collectors.toList());
    }

    public Long createMember(MemberDTO.MemberRequest requestDTO) {
        Member member = userRepository.save(MemberMapper.INSTANCE.memberRquestDtoToMember(requestDTO));
        return member.getId();
    }
}
