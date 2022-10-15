package com.study.boardExample.repository;

import com.study.boardExample.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Modifying
    @Query("update Post p set p.cnt = :cnt where p.id = :postId")
    int updatePostCountById(Long postId, Integer cnt);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("select p from Post p where p.id = :postId")
    Optional<Post> findPostById_Locked_Pessimistic(Long postId);

}
