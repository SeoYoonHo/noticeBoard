package com.study.boardExample.controller;

import com.study.boardExample.common.CommonResponse;
import com.study.boardExample.dto.PostDTO;
import com.study.boardExample.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/post")
@Slf4j
@RequiredArgsConstructor
public class PostController {
    private final String AUTHORIZATION_HEADER = "Authorization";
    private final PostService postService;

    @GetMapping("/search/{id}")
    public ResponseEntity<CommonResponse.DataResponse<PostDTO.PostResponse>> getPostById(
            @PathVariable(value = "id") Long id) {
        PostDTO.PostResponse postResponse = postService.findPostMyId(id);
        return ResponseEntity.ok(CommonResponse.DataResponse.of("001", "Success", postResponse));
    }

    @GetMapping("/{boardType}/search/{id}")
    public ResponseEntity<CommonResponse.DataResponse<PostDTO.PostResponse>> getPostById(
            @PathVariable(value = "boardType") String boardType,
            @PathVariable(value = "id") Long id) {
        PostDTO.PostResponse postResponse = postService.findPostByIdAndBoardType(id, boardType);
        return ResponseEntity.ok(CommonResponse.DataResponse.of("001", "Success", postResponse));
    }

    @GetMapping("/{boardType}/searchList")
    public ResponseEntity<CommonResponse.DataResponse<Page<PostDTO.PostResponse>>> getPostByType(
            @PathVariable(value = "boardType") String boardType,
            @PageableDefault Pageable pageable) {
        Page<PostDTO.PostResponse> postResponses = postService.findPostListByBoardType(boardType, pageable);
        return ResponseEntity.ok(CommonResponse.DataResponse.of("001", "Success", postResponses));
    }

    @PostMapping("/{boardType}/create")
    public ResponseEntity<CommonResponse.DataResponse<HashMap<String, Long>>> createPost(
            @RequestHeader(AUTHORIZATION_HEADER) String bearerToken,
            @PathVariable(value = "boardType") String boardType,
            @RequestBody PostDTO.CreatePostRequest createPostRequest) {
        Long postId = postService.createNewPost(bearerToken, createPostRequest);
        HashMap<String, Long> resMap = new HashMap<>();
        resMap.put("postId", postId);
        return ResponseEntity.ok(CommonResponse.DataResponse.of("002", "Create post success", resMap));
    }

}
