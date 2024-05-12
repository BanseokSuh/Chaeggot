package com.banny.chaeggot.controller;

import com.banny.chaeggot.controller.request.PostCreateRequest;
import com.banny.chaeggot.controller.request.PostModifyRequest;
import com.banny.chaeggot.controller.response.PostCreateResponse;
import com.banny.chaeggot.controller.response.PostResponse;
import com.banny.chaeggot.controller.response.Response;
import com.banny.chaeggot.model.Post;
import com.banny.chaeggot.model.User;
import com.banny.chaeggot.service.PostService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
@RestController
public class PostController {

    private final PostService postService;

    /**
     * Create a post
     *
     * @param request
     * @return
     */
    @PostMapping
    public Response<PostCreateResponse> createPost(@RequestBody PostCreateRequest request, Authentication authentication) {

        // Get the user information from the authentication object.
        User user = (User) authentication.getPrincipal();

        // Create a post.
        Post post = postService.createPost(request.getTitle(), request.getContent(), user.getUserIdx());

        return Response.success(PostCreateResponse.from(post));
    }

    /**
     * Modify a post
     *
     * @param postIdx
     * @param request
     * @param authentication
     * @return
     */
    @PutMapping("/{postIdx}")
    public Response<Void> modifyPost(@PathVariable(name = "postIdx") Long postIdx, @RequestBody PostModifyRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        postService.modifyPost(request.getTitle(), request.getContent(), user.getUserIdx(), postIdx);
        return Response.success();
    }

    /**
     * Delete a post
     *
     * @param postIdx
     * @param authentication
     * @return
     */
    @DeleteMapping("/{postIdx}")
    public Response<Void> deletePost(@PathVariable(name = "postIdx") Long postIdx, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        postService.deletePost(postIdx, user.getUserIdx());
        return Response.success();
    }

    /**
     * Get posts
     *
     * @param pageable
     * @return
     */
    @GetMapping
    public Response<Page<PostResponse>> getPosts(Pageable pageable) {
        return Response.success(postService.getPosts(pageable).map(PostResponse::from));
    }

    /**
     * Get a post
     *
     * @param postIdx
     * @return
     */
    @GetMapping("/{postIdx}")
    public Response<PostResponse> getPost(@PathVariable(name = "postIdx") Long postIdx) {
        return Response.success(PostResponse.from(postService.getPost(postIdx)));
    }
}
