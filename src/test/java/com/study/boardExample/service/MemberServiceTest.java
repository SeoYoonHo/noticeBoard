package com.study.boardExample.service;

import com.study.boardExample.domain.Member;
import com.study.boardExample.dto.MemberDTO;
import com.study.boardExample.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    int beforeCnt;

    @BeforeEach
    void setUp() {
        beforeCnt = memberService.findMemberByName("SYH").size();

        for (int i = 0; i < 10; i++) {
            Member member = new Member();
            member.setName("SYH");
            member.setAge(32 + i);
            member.setGender("male");
            member.setLevel("high");

            //when
            memberRepository.save(member);
        }
    }

    @Test
    void findMemberById() {
        //given
        Member member = new Member();
        member.setName("SYH");
        member.setAge(32);
        member.setGender("male");
        member.setLevel("high");

        //when
        memberRepository.save(member);
        em.flush();

        //then
        MemberDTO.MemberResponse findMember = memberService.findMemberById(member.getId());
        assertEquals(member.getId(), findMember.getId());
    }

    @Test
    void findMemberByName() {
        //then
        assertEquals(beforeCnt + 10, memberService.findMemberByName("SYH").size());
    }

    @Test
    void createMember() {
        //given
        MemberDTO.MemberRequest memberDto = new MemberDTO.MemberRequest();
        memberDto.setName("SYH");
        memberDto.setAge(32);
        memberDto.setGender("male");
        memberDto.setLevel("high");
        memberDto.setTel("010-6330-2643");

        //when
        Long id = memberService.createMember(memberDto);

        //then
        Member res = memberRepository.findById(id).orElseGet(null);
        assertNotNull(res);
    }
}