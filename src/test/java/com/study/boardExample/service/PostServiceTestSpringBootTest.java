package com.study.boardExample.service;

import com.study.boardExample.domain.Post;
import com.study.boardExample.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class PostServiceTestSpringBootTest {
    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;

    @Test
    public void multiThreadTest() throws Exception {
        Instant start = Instant.now();
        //given
        ExecutorService service = Executors.newFixedThreadPool(10);

        int count = 10000;
        CountDownLatch latch = new CountDownLatch(count);

        //when
        for (int i = 0; i < count; i++) {
            service.execute(() -> {
                postService.findPostByIdAndBoardType(1L, "notice");
                latch.countDown();
            });
        }

        latch.await();

        Instant end = Instant.now();
        System.out.println("수행시간: " + Duration.between(start, end).toMillis() + " ms");

        //then
        Post testPost = postRepository.findById(1L).orElseThrow(() -> new Exception("here"));
        assertThat(testPost.getCnt(), equalTo(count));
    }
}
