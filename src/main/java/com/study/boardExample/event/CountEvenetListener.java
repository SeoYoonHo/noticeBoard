package com.study.boardExample.event;

import com.study.boardExample.domain.Post;
import com.study.boardExample.exception.NoSearchException;
import com.study.boardExample.repository.PostRepository;
import com.study.boardExample.service.CountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CountEvenetListener {
    private final PostRepository postRepository;

    private final ConcurrentHashMap<Long, AtomicInteger> postCountHashMap = new ConcurrentHashMap<>();

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void handleCountEvent(CountEvent event) {
        Long postId = event.getId();
        int currentCnt = event.getCnt();

        postCountHashMap.compute(postId, (aLong, integer) -> integer == null ? new AtomicInteger() : integer);
        int count = postCountHashMap.get(postId).incrementAndGet();
        log.debug("count : " + count);

        if (count % 10 == 0) {
            synchronized (CountService.class) {
                postCountHashMap.get(postId).getAndUpdate(operand -> operand - count);
//                postCountHashMap.get(postId).set(0);
                Post lockPost = postRepository.findPostById_Locked_Pessimistic(postId)
                                              .map(post ->
                                              {
                                                  post.setCnt(currentCnt + count);
                                                  return post;
                                              })
                                              .orElseThrow(() -> new NoSearchException("not search post"));
//                log.debug("count : " + count);
//                log.debug("lock Post :" + lockPost.toString());
                postRepository.save(lockPost);
            }
        }
    }
}
