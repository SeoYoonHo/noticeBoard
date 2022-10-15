package com.study.boardExample.service;

import com.study.boardExample.domain.Post;
import com.study.boardExample.exception.NoSearchException;
import com.study.boardExample.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CountService {

    private final PostRepository postRepository;

    private final ConcurrentHashMap<Long, AtomicInteger> postCountHashMap = new ConcurrentHashMap<>();

    public void postCountIncrease(Long postId) {
        postCountHashMap.compute(postId, (aLong, integer) -> integer == null ? new AtomicInteger() : integer);
        int count = postCountHashMap.get(postId).incrementAndGet();

        if (count % 10 == 0) {
            synchronized (CountService.class) {
//                postCountHashMap.get(postId).set(0);
                Post lockPost = postRepository.findPostById_Locked_Pessimistic(postId)
                                              .map(post ->
                                              {
                                                  post.setCnt(count);
                                                  return post;
                                              })
                                              .orElseThrow(() -> new NoSearchException("not search post"));
                log.debug("count : " + count);
                log.debug("lock Post :" + lockPost.toString());
                postRepository.save(lockPost);
            }
        }
    }

    public int getCount(Long postId) {
        return postCountHashMap.get(postId).get();
    }

}
