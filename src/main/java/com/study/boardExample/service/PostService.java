package com.study.boardExample.service;

import com.study.boardExample.domain.Member;
import com.study.boardExample.domain.Post;
import com.study.boardExample.dto.PostDTO;
import com.study.boardExample.enums.BoardTypeEnums;
import com.study.boardExample.event.CountEventPublisher;
import com.study.boardExample.exception.NoSearchException;
import com.study.boardExample.exception.NotMatchException;
import com.study.boardExample.mapper.PostMapper;
import com.study.boardExample.repository.MemberRepository;
import com.study.boardExample.repository.PostRepository;
import com.study.boardExample.utils.JsonWebTokenIssuer;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public PostDTO.PostResponse findPostByIdAndBoardType(Long id, String boardType) {
        Optional<Post> optionalPost = postRepository.findByIdAndBoardType(id, BoardTypeEnums.getBoardType(boardType));
        PostDTO.PostResponse response = optionalPost.map(PostMapper.INSTANCE::postToPostResponseDto)
                                                    .orElseThrow(() -> new NoSearchException("No search post"));

        countEventPublisher.publish(id);
        return response;
    }

    public Page<PostDTO.PostResponse> findPostListByBoardType(String boardType, Pageable pageable) {
        return postRepository.findAllByBoardType(BoardTypeEnums.getBoardType(boardType), pageable)
                             .map(PostMapper.INSTANCE::postToPostResponseDto);
    }

    public Long createNewPost(String bearerToken, String boardType, PostDTO.CreatePostRequest createPostRequest) {
        Claims claims = jsonWebTokenIssuer.parseClaimsFromBearerAccessToken(bearerToken);

        Member member = memberRepository.findMemberByEmail(claims.getSubject())
                                        .orElseThrow(() -> new NoSearchException("member email is not found"));

        Post createPost = PostMapper.INSTANCE.postCreateRequestDtoToPost(createPostRequest);
        createPost.setMember(member);
        createPost.setBoardType(BoardTypeEnums.getBoardType(boardType));
        Post post = postRepository.save(createPost);

        return post.getId();
    }

    public Long updatePost(String bearerToken, String boardType, PostDTO.UpdatePostRequest updatePostRequest) {
        Claims claims = jsonWebTokenIssuer.parseClaimsFromBearerAccessToken(bearerToken);

        Post updatePost = postRepository.findByIdAndBoardType(updatePostRequest.getId(),
                BoardTypeEnums.getBoardType(boardType)).orElseThrow(() -> new NoSearchException("No search post"));
        updatePost.setContents(updatePostRequest.getContents());

        if(!claims.getSubject().equals(updatePost.getMember().getEmail())){
            throw new NotMatchException("Not Match Post Owner!");
        }

        return updatePost.getId();
    }

}
