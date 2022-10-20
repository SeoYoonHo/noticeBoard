package com.study.boardExample.repository;

import com.study.boardExample.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Post p where p.id = :postId")
    Optional<Post> findPostById_Locked_Pessimistic(Long postId);

    @Query("select p from Post p left join fetch p.boardType b where p.id = :postId and b.typeName = :boardTypeName")
    Optional<Post> findByIdAndBoardTypeName(Long postId, String boardTypeName);

    @Query(value = "select p from Post p join fetch p.boardType b where b.typeName = :boardTypeName",
            countQuery = "select count(p) from Post p join p.boardType b where b.typeName = :boardTypeName")
    Page<Post> findAllByBoardTypeName(String boardTypeName, Pageable pageable);
}
