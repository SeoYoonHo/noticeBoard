package com.study.boardExample.service;

import com.study.boardExample.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CountService {

    private final PostRepository postRepository;

    private final ConcurrentHashMap<Long, Integer> postCountHashMap = new ConcurrentHashMap<>();


    public void postCountIncrease(Long postId) {
        postCountHashMap.compute(postId, (aLong, integer) -> integer == null ? 1 : integer + 1);

        if (postCountHashMap.get(postId) > 9) {
            int cnt = postCountHashMap.get(postId);
            postCountHashMap.replace(postId, 0);
            postRepository.updatePostCountById(postId, cnt);
        }
    }

}
