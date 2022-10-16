package com.study.boardExample.service;

import com.study.boardExample.domain.Post;
import com.study.boardExample.dto.PostDTO;
import com.study.boardExample.event.CountEventPublisher;
import com.study.boardExample.exception.NoSearchException;
import com.study.boardExample.mapper.PostMapper;
import com.study.boardExample.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final CountService countService;
    private final CountEventPublisher countEventPublisher;

    public PostDTO.PostResponse findPostMyId(Long id) {
        Optional<Post> optionalPost = postRepository.findPostById_Locked_Pessimistic(id);
        PostDTO.PostResponse response = optionalPost
                .map(PostMapper.INSTANCE::postToPostResponseDto)
                .orElseThrow(() -> new NoSearchException("No search post"));
        Post incCntPost = optionalPost.map(post ->
                                      {
                                          post.setCnt(post.getCnt() + 1);
                                          return post;
                                      })
                                      .orElseThrow(
                                              () -> new NoSearchException("not search post"));
        postRepository.save(incCntPost);
//        countService.postCountIncrease(id);
//        countEventPublisher.publish(id, response.getCnt());
        return response;
    }

    public Long createNewPost(PostDTO.CreatePostRequest createPostRequest) {
        Post post = postRepository.save(PostMapper.INSTANCE.postCreateRequestDtoToPost(createPostRequest));
        return post.getId();
    }

}
