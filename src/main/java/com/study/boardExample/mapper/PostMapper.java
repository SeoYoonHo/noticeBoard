package com.study.boardExample.mapper;

import com.study.boardExample.domain.Post;
import com.study.boardExample.dto.PostDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(target = "email", source = "post.member.email")
    PostDTO.PostResponse postToPostResponseDto(Post post);

    Post postCreateRequestDtoToPost(PostDTO.CreatePostRequest createPostRequest);
}
