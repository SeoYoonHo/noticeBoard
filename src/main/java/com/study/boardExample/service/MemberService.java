package com.study.boardExample.service;

import com.study.boardExample.domain.Member;
import com.study.boardExample.dto.MemberDTO;
import com.study.boardExample.exception.NoSearchException;
import com.study.boardExample.mapper.MemberMapper;
import com.study.boardExample.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberDTO.MemberResponse findMemberById(Long id) {
        return memberRepository.findById(id).map(MemberMapper.INSTANCE::memeberToMemberResponseDto)
                               .orElseThrow(() -> new NoSearchException("No search member"));
    }

    @Transactional(readOnly = true)
    public List<MemberDTO.MemberResponse> findMemberByName(String name) {
        List<Member> memberList = memberRepository.findByName(name);
        return memberList.stream()
                         .map(MemberMapper.INSTANCE::memeberToMemberResponseDto).collect(Collectors.toList());
    }

    public Long createMember(MemberDTO.CreateMemberRequest requestDTO) {
        Member member = memberRepository.save(MemberMapper.INSTANCE.memberCreateRquestDtoToMember(requestDTO));
        return member.getId();
    }

    public Long updateMember(MemberDTO.UpdateMemberRequest requestDTO) {
        Member member = memberRepository.save(MemberMapper.INSTANCE.memberUpdateRquestDtoToMember(requestDTO));
        return member.getId();
    }

    public void deleteMemberById(Long id) {
        memberRepository.deleteById(id);
    }

}
