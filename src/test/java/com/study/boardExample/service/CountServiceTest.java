package com.study.boardExample.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class CountServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    CountService countService;

    @Test
    public void givenMultiThread_whenGetPost_thenIncreateCnt() throws Exception {
        ExecutorService service = Executors.newFixedThreadPool(10);
        AtomicInteger successCount = new AtomicInteger();
        int executeCnt = 100;

        CountDownLatch latch = new CountDownLatch(executeCnt);
        for (int i = 0; i < executeCnt; i++) {
            service.execute(() -> {
                try {
                    postService.findPostMyId(1L);
                    successCount.getAndIncrement();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                latch.countDown();
            });
        }

        latch.await();

        int remainCnt = countService.getCount(1L);

        assertThat(successCount.get(), equalTo(postService.findPostMyId(1L).getCnt()));
    }

}