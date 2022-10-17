package com.study.boardExample.event;

import com.study.boardExample.domain.Post;
import com.study.boardExample.exception.NoSearchException;
import com.study.boardExample.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
@Slf4j
public class CountEvenetListener {
    private final PostRepository postRepository;

    private final ConcurrentHashMap<Long, AtomicInteger> postCountHashMap = new ConcurrentHashMap<>();

    @Async
    @EventListener
    public void handleCountEvent(CountEvent event) {
        Long postId = event.getId();

        postCountHashMap.compute(postId, (aLong, integer) -> integer == null ? new AtomicInteger(1) : integer);
        int count = postCountHashMap.get(postId).incrementAndGet();
        if (count % 10 == 0) {
            postCountHashMap.get(postId).updateAndGet(n -> n - count);
            Post lockPost = postRepository.findPostById_Locked_Pessimistic(postId)
                                          .map(post ->
                                          {
                                              post.setCnt(post.getCnt() + count);
                                              return post;
                                          })
                                          .orElseThrow(
                                                  () -> new NoSearchException("not search post"));
            log.debug("count : " + count);
            log.debug("lock Post :" + lockPost.toString());
        }
    }
}
