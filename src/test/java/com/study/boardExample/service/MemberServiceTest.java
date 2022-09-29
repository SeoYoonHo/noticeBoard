package com.study.boardExample.service;

import com.study.boardExample.domain.Member;
import com.study.boardExample.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    void findMemberById() {
        //given
        Member member = new Member();
        member.setAge(32);
        member.setGender("male");
        member.setLevel("high");
        member.setCreateDt(LocalDate.now());
        member.setLastLoginDt(LocalDate.now());

        memberRepository.save(member);

        //when
//        MemberDTO.MemberResponse findMember = memberService.findMemberById(member.getId());
        Member findMember = memberRepository.findById(member.getId()).get();

        //then
        assertEquals(member.getId(), findMember.getId());
    }
}