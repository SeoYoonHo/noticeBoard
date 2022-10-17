package com.study.boardExample.service;

import com.study.boardExample.domain.Member;
import com.study.boardExample.domain.Post;
import com.study.boardExample.dto.PostDTO;
import com.study.boardExample.event.CountEventPublisher;
import com.study.boardExample.exception.NoSearchException;
import com.study.boardExample.mapper.PostMapper;
import com.study.boardExample.repository.MemberRepository;
import com.study.boardExample.repository.PostRepository;
import com.study.boardExample.utils.JsonWebTokenIssuer;
import io.jsonwebtoken.Claims;
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
    private final CountEventPublisher countEventPublisher;
    private final JsonWebTokenIssuer jsonWebTokenIssuer;
    private final MemberRepository memberRepository;

    public PostDTO.PostResponse findPostMyId(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        PostDTO.PostResponse response = optionalPost.map(PostMapper.INSTANCE::postToPostResponseDto)
                                                    .orElseThrow(() -> new NoSearchException("No search post"));

        countEventPublisher.publish(id);
        return response;
    }

    public Long createNewPost(String bearerToken, PostDTO.CreatePostRequest createPostRequest) {
        Claims claims = jsonWebTokenIssuer.parseClaimsFromBearerAccessToken(bearerToken);

        Member member = memberRepository.findMemberByEmail(claims.getSubject())
                                        .orElseThrow(() -> new NoSearchException("member email is not found"));

        Post createPost = PostMapper.INSTANCE.postCreateRequestDtoToPost(createPostRequest);
        createPost.setMember(member);
        Post post = postRepository.save(createPost);

        return post.getId();
    }

}
