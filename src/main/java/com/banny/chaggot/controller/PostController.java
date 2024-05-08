package com.banny.chaggot.controller;

import com.banny.chaggot.controller.request.PostCreateRequest;
import com.banny.chaggot.controller.request.PostModifyRequest;
import com.banny.chaggot.controller.response.PostCreateResponse;
import com.banny.chaggot.controller.response.Response;
import com.banny.chaggot.model.User;
import com.banny.chaggot.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
@RestController
public class PostController {

    private final PostService postService;

    /**
     * Create post
     * @param request
     * @return
     */
    @PostMapping
    public Response<PostCreateResponse> createPost(@RequestBody PostCreateRequest request, Authentication authentication) {

        // Get the user information from the authentication object.
        User user = (User) authentication.getPrincipal();

        // Create a post.
        Long postId = postService.createPost(request.getTitle(), request.getContent(), user.getUserId());

        return Response.success(PostCreateResponse.of(postId));
    }

    /**
     * Modify post
     * @param postIdx
     * @param request
     * @param authentication
     * @return
     */
    @PutMapping("/{postIdx}")
    public Response<Void> modifyPost(@PathVariable(name = "postIdx") Long postIdx, @RequestBody PostModifyRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        postService.modifyPost(request.getTitle(), request.getContent(), user.getUserId(), postIdx);
        return Response.success();
    }
}
