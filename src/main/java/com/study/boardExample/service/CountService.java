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
        Post lockPost = postRepository.findPostById_Locked_Pessimistic(postId)
                                      .map(post ->
                                      {
                                          post.setCnt(post.getCnt() + 1);
                                          return post;
                                      })
                                      .orElseThrow(
                                              () -> new NoSearchException("not search post"));
        log.debug("lock Post :" + lockPost.toString());
        postRepository.save(lockPost);
//        postCountHashMap.compute(postId, (aLong, integer) -> integer == null ? new AtomicInteger() : integer);
//        postCountHashMap.get(postId)
//                        .incrementAndGet();
//        postCountHashMap.get(postId)
//                        .updateAndGet(n -> {
//                            if (n % 10 == 0) {
//                                Post lockPost = postRepository.findPostById_Locked_Pessimistic(postId)
//                                                              .map(post ->
//                                                              {
//                                                                  post.setCnt(post.getCnt() + n);
//                                                                  return post;
//                                                              })
//                                                              .orElseThrow(
//                                                                      () -> new NoSearchException("not search post"));
//                                log.debug("count : " + n);
//                                log.debug("lock Post :" + lockPost.toString());
//                                postRepository.save(lockPost);
//                                return 0;
//                            } else {
//                                return n;
//                            }
//                        });
    }

    public int getCount(Long postId) {
        return postCountHashMap.get(postId)
                               .get();
    }

}
