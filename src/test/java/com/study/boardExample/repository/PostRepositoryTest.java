package com.study.boardExample.repository;

import com.study.boardExample.domain.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
class PostRepositoryTest {
    @Autowired
    PostRepository postRepository;

    @Test
    public void givenPostIdAndBoardTypeName_whenFindByIdAndBoardTypeName_thenReturnPostEntity() {
        Optional<Post> optionalPost = postRepository.findByIdAndBoardTypeName(1L, "notice");
        Post post = optionalPost.orElse(null);

        assertThat(post, notNullValue());
        assertThat(post.getContents(), equalTo("contents1"));

    }

    @Test
    public void givenBoardTypeName_whenfindAllByTypeName_thenReturnBoardType() {
        Pageable pageable = PageRequest.of(1, 10);
        Page<Post> boardTypes = postRepository.findAllByBoardTypeName("notice", pageable);

        assertThat(boardTypes, notNullValue());
        assertThat(boardTypes.getSize(), lessThanOrEqualTo(10));
        assertThat(boardTypes.getContent().get(0).getContents(), equalTo("contents1"));
    }
}