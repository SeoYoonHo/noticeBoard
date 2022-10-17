package com.study.boardExample.service;

import com.study.boardExample.domain.Member;
import com.study.boardExample.domain.Post;
import com.study.boardExample.dto.PostDTO;
import com.study.boardExample.event.CountEventPublisher;
import com.study.boardExample.mapper.PostMapper;
import com.study.boardExample.repository.MemberRepository;
import com.study.boardExample.repository.PostRepository;
import com.study.boardExample.utils.JsonWebTokenIssuer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    PostRepository postRepository;
    @Mock
    CountEventPublisher countEventPublisher;
    @Mock
    JsonWebTokenIssuer jsonWebTokenIssuer;
    @Mock
    MemberRepository memberRepository;

    private final String KEY_ROLES = "roles";

    @InjectMocks
    PostService postService;


    @Test
    public void givenPostContents_whenCreatePost_thenReturnPostId() {
        String bearerToken = "bearerToken";
        Claims claims = Jwts.claims().setSubject("gogoy2643@naver.com");
        claims.put(KEY_ROLES, Collections.singleton("ROLD_ADMIN"));

        Member member = new Member();
        member.setAuthority("ROLD_ADMIN");
        member.setEmail("gogoy2643@naver.com");
        Optional<Member> optionalMember = Optional.of(member);

        PostDTO.CreatePostRequest createPostRequest = new PostDTO.CreatePostRequest();
        createPostRequest.setContents("test!!!!");

        Post post = new Post();
        post.setId(1L);

        when(jsonWebTokenIssuer.parseClaimsFromBearerAccessToken(bearerToken)).thenReturn(claims);
        when(memberRepository.findMemberByEmail(claims.getSubject())).thenReturn(optionalMember);
        when(postRepository.save(PostMapper.INSTANCE.postCreateRequestDtoToPost(createPostRequest))).thenReturn(post);

        Long postId = postService.createNewPost(bearerToken, createPostRequest);

        assertThat(postId, is(notNullValue()));
    }

}