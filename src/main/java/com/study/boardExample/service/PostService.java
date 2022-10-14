package com.study.boardExample.service;

import com.study.boardExample.domain.Post;
import com.study.boardExample.dto.PostDTO;
import com.study.boardExample.exception.NoSearchException;
import com.study.boardExample.mapper.PostMapper;
import com.study.boardExample.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final CountService countService;

    public PostDTO.PostResponse findPostMyId(Long id) {
        PostDTO.PostResponse response = postRepository.findById(id)
                                                      .map(PostMapper.INSTANCE::postToPostResponseDto)
                                                      .orElseThrow(() -> new NoSearchException("No search post"));
        countService.postCountIncrease(id);
        return response;
    }

    public Long createNewPost(PostDTO.CreatePostRequest createPostRequest) {
        Post post = postRepository.save(PostMapper.INSTANCE.postCreateRequestDtoToPost(createPostRequest));
        return post.getId();
    }

}
