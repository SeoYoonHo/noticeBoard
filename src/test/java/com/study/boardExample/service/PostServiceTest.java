package com.study.boardExample.service;

import com.study.boardExample.domain.Member;
import com.study.boardExample.domain.Post;
import com.study.boardExample.dto.PostDTO;
import com.study.boardExample.enums.BoardTypeEnums;
import com.study.boardExample.exception.InvalidBoardTypeException;
import com.study.boardExample.exception.NoSearchException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    PostRepository postRepository;
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

        Post createPost = PostMapper.INSTANCE.postCreateRequestDtoToPost(createPostRequest);
        createPost.setMember(member);

        Post post = new Post();
        post.setId(1L);

        when(jsonWebTokenIssuer.parseClaimsFromBearerAccessToken(bearerToken)).thenReturn(claims);
        when(memberRepository.findMemberByEmail(claims.getSubject())).thenReturn(optionalMember);
        when(postRepository.save(createPost)).thenReturn(post);

        Long postId = postService.createNewPost(bearerToken, createPostRequest);

        assertThat(postId, is(notNullValue()));
    }

    @Test
    public void givenInvalidEamilUser_whenCreatePost_thenThrowNoSearchException() {
        String bearerToken = "bearerToken";
        Claims claims = Jwts.claims().setSubject("gogoy2643@naver.com");
        claims.put(KEY_ROLES, Collections.singleton("ROLD_ADMIN"));

        PostDTO.CreatePostRequest createPostRequest = new PostDTO.CreatePostRequest();
        createPostRequest.setContents("test!!!!");

        when(jsonWebTokenIssuer.parseClaimsFromBearerAccessToken(bearerToken)).thenReturn(claims);
        when(memberRepository.findMemberByEmail(claims.getSubject())).thenReturn(Optional.empty());

        Throwable throwable = assertThrows(NoSearchException.class,
                () -> postService.createNewPost(bearerToken, createPostRequest));

        assertThat(throwable, isA(NoSearchException.class));
        assertThat(throwable.getMessage(), equalTo("member email is not found"));
    }

    @Test
    public void givenInvalidBoardTypeString_whenFindPostByIdAndBoardType_thenInavalidBoardTypeException() {
        Long paramId = 1L;
        String invalidBoardType = "invalidBoardType";
        Optional<Post> optionalPost = Optional.empty();

        when(postRepository.findByIdAndBoardType(paramId, BoardTypeEnums.getBoardType(invalidBoardType))).thenReturn(optionalPost);

        Throwable throwable = assertThrows(InvalidBoardTypeException.class,
                () -> postService.findPostByIdAndBoardType(paramId, invalidBoardType));

        assertThat(throwable, isA(InvalidBoardTypeException.class));
        assertThat(throwable.getMessage(), equalTo("Invalid Board Type!"));
    }

    @Test
    public void givenInvalidBoardTypeString_whenFindPostListByBoardType_thenInavalidBoardTypeException() {
        String invalidBoardType = "invalidBoardType";
        Pageable pageable = PageRequest.of(1, 10);
        Page<Post> postResponses = Page.empty();

        when(postRepository.findAllByBoardType(BoardTypeEnums.getBoardType(invalidBoardType),pageable)).thenReturn(postResponses);

        Throwable throwable = assertThrows(InvalidBoardTypeException.class,
                () -> postService.findPostListByBoardType(invalidBoardType, pageable));

        assertThat(throwable, isA(InvalidBoardTypeException.class));
        assertThat(throwable.getMessage(), equalTo("Invalid Board Type!"));
    }

}