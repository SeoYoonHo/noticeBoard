package com.study.boardExample.repository;

import com.study.boardExample.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>{
    @Override
    Optional<Member> findById(Long aLong);

    Optional<Member> findMemberByEmail(String email);

    List<Member> findByName(String name);
}
