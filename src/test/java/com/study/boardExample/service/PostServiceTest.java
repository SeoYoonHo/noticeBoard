package com.study.boardExample.service;

import com.study.boardExample.domain.Member;
import com.study.boardExample.domain.Post;
import com.study.boardExample.dto.PostDTO;
import com.study.boardExample.enums.BoardTypeEnums;
import com.study.boardExample.exception.InvalidBoardTypeException;
import com.study.boardExample.exception.NoSearchException;
import com.study.boardExample.exception.NotMatchException;
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

        String boardType = "notice";

        Member member = new Member();
        member.setAuthority("ROLD_ADMIN");
        member.setEmail("gogoy2643@naver.com");
        Optional<Member> optionalMember = Optional.of(member);

        PostDTO.CreatePostRequest createPostRequest = new PostDTO.CreatePostRequest();
        createPostRequest.setContents("test!!!!");

        Post createPost = PostMapper.INSTANCE.postCreateRequestDtoToPost(createPostRequest);
        createPost.setMember(member);
        createPost.setBoardType(BoardTypeEnums.getBoardType(boardType));

        Post post = new Post();
        post.setId(1L);

        when(jsonWebTokenIssuer.parseClaimsFromBearerAccessToken(bearerToken)).thenReturn(claims);
        when(memberRepository.findMemberByEmail(claims.getSubject())).thenReturn(optionalMember);
        when(postRepository.save(createPost)).thenReturn(post);

        Long postId = postService.createNewPost(bearerToken, boardType, createPostRequest);

        assertThat(postId, is(notNullValue()));
    }

    @Test
    public void givenInvalidEamilUser_whenCreatePost_thenThrowNoSearchException() {
        String bearerToken = "bearerToken";
        Claims claims = Jwts.claims().setSubject("gogoy2643@naver.com");
        claims.put(KEY_ROLES, Collections.singleton("ROLD_ADMIN"));

        String boardType = "notice";

        PostDTO.CreatePostRequest createPostRequest = new PostDTO.CreatePostRequest();
        createPostRequest.setContents("test!!!!");

        when(jsonWebTokenIssuer.parseClaimsFromBearerAccessToken(bearerToken)).thenReturn(claims);
        when(memberRepository.findMemberByEmail(claims.getSubject())).thenReturn(Optional.empty());

        Throwable throwable = assertThrows(NoSearchException.class,
                () -> postService.createNewPost(bearerToken, boardType, createPostRequest));

        assertThat(throwable, isA(NoSearchException.class));
        assertThat(throwable.getMessage(), equalTo("member email is not found"));
    }

    @Test
    public void givenInavlidPostId_whenUpdatePost_thenThrowNoSearchException() {
        String bearerToken = "bearerToken";
        Claims claims = Jwts.claims().setSubject("gogoy2643@naver.com");
        claims.put(KEY_ROLES, Collections.singleton("ROLD_ADMIN"));
        Optional<Post> optionalPost = Optional.empty();
        String boardType = "notice";
        PostDTO.UpdatePostRequest updatePostRequest = new PostDTO.UpdatePostRequest();
        updatePostRequest.setId(1L);
        updatePostRequest.setContents("test");

        when(jsonWebTokenIssuer.parseClaimsFromBearerAccessToken(bearerToken)).thenReturn(claims);
        when(postRepository.findByIdAndBoardType(updatePostRequest.getId(),
                BoardTypeEnums.getBoardType(boardType))).thenReturn(optionalPost);

        Throwable throwable = assertThrows(NoSearchException.class,
                () -> postService.updatePost(bearerToken, boardType, updatePostRequest));

        assertThat(throwable, isA(NoSearchException.class));
        assertThat(throwable.getMessage(), equalTo("No search post"));
    }

    @Test
    public void givenInvalidBoardType_whenUpdatePost_thenThrowInvalidBoardTypeException() {
        String bearerToken = "bearerToken";
        Claims claims = Jwts.claims().setSubject("gogoy2643@naver.com");
        claims.put(KEY_ROLES, Collections.singleton("ROLD_ADMIN"));
        String boardType = "invalid Board Type";
        PostDTO.UpdatePostRequest updatePostRequest = new PostDTO.UpdatePostRequest();
        updatePostRequest.setId(1L);
        updatePostRequest.setContents("test");

        when(jsonWebTokenIssuer.parseClaimsFromBearerAccessToken(bearerToken)).thenReturn(claims);

        Throwable throwable = assertThrows(InvalidBoardTypeException.class,
                () -> postService.updatePost(bearerToken, boardType, updatePostRequest));

        assertThat(throwable, isA(InvalidBoardTypeException.class));
        assertThat(throwable.getMessage(), equalTo("Invalid Board Type!!"));
    }

    @Test
    public void givenNotOwnerUser_whenUpdatePost_thenThrowNotMatchException() {
        String bearerToken = "bearerToken";
        Claims claims = Jwts.claims().setSubject("gogoy2643@naver.com");
        claims.put(KEY_ROLES, Collections.singleton("ROLD_ADMIN"));

        Post findPost = new Post();
        Member member = new Member();
        member.setEmail("notMatchEmail");
        findPost.setMember(member);
        Optional<Post> optionalPost = Optional.of(findPost);

        String boardType = "notice";
        PostDTO.UpdatePostRequest updatePostRequest = new PostDTO.UpdatePostRequest();
        updatePostRequest.setId(1L);
        updatePostRequest.setContents("test");

        when(jsonWebTokenIssuer.parseClaimsFromBearerAccessToken(bearerToken)).thenReturn(claims);
        when(postRepository.findByIdAndBoardType(updatePostRequest.getId(),
                BoardTypeEnums.getBoardType(boardType))).thenReturn(optionalPost);

        Throwable throwable = assertThrows(NotMatchException.class,
                () -> postService.updatePost(bearerToken, boardType, updatePostRequest));

        assertThat(throwable, isA(NotMatchException.class));
        assertThat(throwable.getMessage(), equalTo("Not Match Post Owner!"));
    }

    @Test
    public void givenInavlidPostId_whenDeletePost_thenThrowNoSearchException() {
        String bearerToken = "bearerToken";
        Claims claims = Jwts.claims().setSubject("gogoy2643@naver.com");
        claims.put(KEY_ROLES, Collections.singleton("ROLD_ADMIN"));
        Optional<Post> optionalPost = Optional.empty();
        String boardType = "notice";
        PostDTO.DeletePostRequest deletePostRequest = new PostDTO.DeletePostRequest();
        deletePostRequest.setId(1L);

        when(jsonWebTokenIssuer.parseClaimsFromBearerAccessToken(bearerToken)).thenReturn(claims);
        when(postRepository.findByIdAndBoardType(deletePostRequest.getId(),
                BoardTypeEnums.getBoardType(boardType))).thenReturn(optionalPost);

        Throwable throwable = assertThrows(NoSearchException.class,
                () -> postService.deletePost(bearerToken, boardType, deletePostRequest));

        assertThat(throwable, isA(NoSearchException.class));
        assertThat(throwable.getMessage(), equalTo("No search post"));
    }

    @Test
    public void givenInvalidBoardType_whenDeletePost_thenThrowInvalidBoardTypeException() {
        String bearerToken = "bearerToken";
        Claims claims = Jwts.claims().setSubject("gogoy2643@naver.com");
        claims.put(KEY_ROLES, Collections.singleton("ROLD_ADMIN"));
        String boardType = "invalid Board Type";
        PostDTO.DeletePostRequest deletePostRequest = new PostDTO.DeletePostRequest();
        deletePostRequest.setId(1L);

        when(jsonWebTokenIssuer.parseClaimsFromBearerAccessToken(bearerToken)).thenReturn(claims);

        Throwable throwable = assertThrows(InvalidBoardTypeException.class,
                () -> postService.deletePost(bearerToken, boardType, deletePostRequest));

        assertThat(throwable, isA(InvalidBoardTypeException.class));
        assertThat(throwable.getMessage(), equalTo("Invalid Board Type!!"));
    }

    @Test
    public void givenNotOwnerUser_whenDeletePost_thenThrowNotMatchException() {
        String bearerToken = "bearerToken";
        Claims claims = Jwts.claims().setSubject("gogoy2643@naver.com");
        claims.put(KEY_ROLES, Collections.singleton("ROLD_ADMIN"));

        Post findPost = new Post();
        Member member = new Member();
        member.setEmail("notMatchEmail");
        findPost.setMember(member);
        Optional<Post> optionalPost = Optional.of(findPost);

        String boardType = "notice";
        PostDTO.DeletePostRequest deletePostRequest = new PostDTO.DeletePostRequest();
        deletePostRequest.setId(1L);

        when(jsonWebTokenIssuer.parseClaimsFromBearerAccessToken(bearerToken)).thenReturn(claims);
        when(postRepository.findByIdAndBoardType(deletePostRequest.getId(),
                BoardTypeEnums.getBoardType(boardType))).thenReturn(optionalPost);

        Throwable throwable = assertThrows(NotMatchException.class,
                () -> postService.deletePost(bearerToken, boardType, deletePostRequest));

        assertThat(throwable, isA(NotMatchException.class));
        assertThat(throwable.getMessage(), equalTo("Not Match Post Owner!"));
    }

    @Test
    public void givenInvalidBoardTypeString_whenFindPostByIdAndBoardType_thenInavalidBoardTypeException() {
        Long paramId = 1L;
        String invalidBoardType = "invalidBoardType";

        Throwable throwable = assertThrows(InvalidBoardTypeException.class,
                () -> postService.findPostByIdAndBoardType(paramId, invalidBoardType));

        assertThat(throwable, isA(InvalidBoardTypeException.class));
        assertThat(throwable.getMessage(), equalTo("Invalid Board Type!!"));
    }

    @Test
    public void givenInvalidBoardTypeString_whenFindPostListByBoardType_thenInavalidBoardTypeException() {
        String invalidBoardType = "invalidBoardType";
        Pageable pageable = PageRequest.of(1, 10);

        Throwable throwable = assertThrows(InvalidBoardTypeException.class,
                () -> postService.findPostListByBoardType(invalidBoardType, pageable));

        assertThat(throwable, isA(InvalidBoardTypeException.class));
        assertThat(throwable.getMessage(), equalTo("Invalid Board Type!!"));
    }

}