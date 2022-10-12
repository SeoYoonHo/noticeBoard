package com.study.boardExample.repository;

import com.study.boardExample.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Test
    public void givenEmail_whenFindById_thenReturnMemberEntity() {
        Optional<Member> optionalMember = memberRepository.findMemberByEmail("gogoy2643@naver.com");
        Member member = optionalMember.orElse(null);

        assertThat(member, notNullValue());
        assertThat(member.getEmail(), equalTo("gogoy2643@naver.com"));
        assertThat(passwordEncoder.matches("123", member.getPassword()), equalTo(true));
        assertThat(member.getAuthority(), equalTo("ROLE_ADMIN"));
    }
}