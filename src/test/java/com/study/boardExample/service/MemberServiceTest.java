package com.study.boardExample.service;

import com.study.boardExample.domain.Member;
import com.study.boardExample.dto.MemberDTO;
import com.study.boardExample.exception.NoSearchException;
import com.study.boardExample.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;
    @InjectMocks
    MemberService memberService;

    @Test
    public void givenNotExistsUserId_whenFindMemberById_thenThrowNoSearchException() {
        Long notExistId = 1L;

        Optional<Member> optionalMemberResponse = Optional.empty();
        when(memberRepository.findById(notExistId)).thenReturn(optionalMemberResponse);

        Throwable throwable = assertThrows(NoSearchException.class, () -> memberService.findMemberById(notExistId));

        assertThat(throwable, isA(NoSearchException.class));
        assertThat(throwable.getMessage(), equalTo("No search member"));
    }

    @Test
    public void givenNotExistsName_whenFindMemberByName_thenReturnEmptyList() {
        String notExistsName = "not exists";

        List<Member> emptyMemberList = new ArrayList<>();
        when(memberRepository.findByName(notExistsName)).thenReturn(emptyMemberList);

        List<MemberDTO.MemberResponse> memeberList = memberService.findMemberByName(notExistsName);

        assertThat(memeberList.size(), equalTo(0));
    }

}