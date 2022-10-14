package com.study.boardExample.repository;

import com.study.boardExample.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Modifying
    @Query("update Post p set p.cnt = :cnt where p.id = :postId")
    int updatePostCountById(Long postId, Integer cnt);

}
