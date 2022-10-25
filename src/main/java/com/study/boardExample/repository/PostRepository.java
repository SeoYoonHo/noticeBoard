package com.study.boardExample.repository;

import com.study.boardExample.domain.Post;
import com.study.boardExample.enums.BoardTypeEnums;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndBoardType(Long postId, BoardTypeEnums boardTypeName);

    Page<Post> findAllByBoardType(BoardTypeEnums boardTypeName, Pageable pageable);

    @Modifying
    @Query("update Post p set p.cnt = p.cnt + :count where p.id=:postId")
    int increasCountParam(Long postId, int count);
}
