package com.banny.springboot.controller;

import com.banny.springboot.controller.request.PostCreateRequest;
import com.banny.springboot.controller.response.PostCreateResponse;
import com.banny.springboot.controller.response.Response;
import com.banny.springboot.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
@RestController
public class PostController {

    private final PostService postService;

    /**
     * 게시글 생성
     * @param request
     * @return
     */
    @PostMapping
    public Response<PostCreateResponse> createPost(@RequestBody PostCreateRequest request) {
        Long postId = postService.createPost(request.getTitle(), request.getContent());
        return Response.success(PostCreateResponse.of(postId));
    }
}
