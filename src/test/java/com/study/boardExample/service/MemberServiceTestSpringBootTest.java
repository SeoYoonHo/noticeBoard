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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class MemberServiceTestSpringBootTest {

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
        MemberDTO.CreateMemberRequest memberDto = new MemberDTO.CreateMemberRequest();
        memberDto.setName("SYH");
        memberDto.setAge(32);
        memberDto.setGender("male");
        memberDto.setLevel("high");
        memberDto.setTel("010-6330-2643");

        //when
        Long id = memberService.createMember(memberDto);

        //then
        Member res = memberRepository.findById(id).orElse(null);
        assertNotNull(res);
    }

    @Test
    void updateMember() {
        //given
        MemberDTO.CreateMemberRequest memberCreateDto = new MemberDTO.CreateMemberRequest();
        memberCreateDto.setName("SYH");
        memberCreateDto.setAge(32);
        memberCreateDto.setGender("male");
        memberCreateDto.setLevel("high");
        memberCreateDto.setTel("010-6330-2643");

        Long id = memberService.createMember(memberCreateDto);
        em.flush();

        MemberDTO.UpdateMemberRequest memberUpdateDto = new MemberDTO.UpdateMemberRequest();
        memberUpdateDto.setId(id);
        memberUpdateDto.setName("SYHUpdate");
        memberUpdateDto.setAge(500);
        memberUpdateDto.setGender("female");
        memberUpdateDto.setLevel("middle");
        memberUpdateDto.setTel("010-6330-2655");


        //when
        memberService.updateMember(memberUpdateDto);
        em.flush();

        //then
        Member res = memberRepository.findById(id).orElse(null);
        assertNotNull(res);

        assertEquals(memberUpdateDto.getName(), res.getName());
        assertEquals(memberUpdateDto.getAge(), res.getAge());
        assertEquals(memberUpdateDto.getGender(), res.getGender());
        assertEquals(memberUpdateDto.getLevel(), res.getLevel());
        assertEquals(memberUpdateDto.getTel(), res.getTel());
    }

    @Test
    void deleteMemberById() {
        //given
        MemberDTO.CreateMemberRequest memberDto = new MemberDTO.CreateMemberRequest();
        memberDto.setName("SYH");
        memberDto.setAge(32);
        memberDto.setGender("male");
        memberDto.setLevel("high");
        memberDto.setTel("010-6330-2643");

        Long id = memberService.createMember(memberDto);
        em.flush();

        //when
        Member deleteBefore = memberRepository.findById(id).orElse(null);
        memberService.deleteMemberById(id);
        Member deleteAfter = memberRepository.findById(id).orElse(null);

        //then
        assertNotNull(deleteBefore);
        assertNull(deleteAfter);
    }
}