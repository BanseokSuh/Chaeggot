package com.banny.chaeggot.service;

import com.banny.chaeggot.exception.ApplicationException;
import com.banny.chaeggot.exception.ErrorCode;
import com.banny.chaeggot.entity.Post;
import com.banny.chaeggot.entity.User;
import com.banny.chaeggot.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    /**
     * Create a post
     *
     * @param title
     * @param content
     * @param userId
     * @return
     */
    public Post createPost(String title, String content, Long userId) {
        User user = userService.getUserEntityOrException(userId);

        return postRepository.save(Post.of(title, content, user));
    }

    /**
     * Modify a post
     *
     * @param title
     * @param content
     * @param userId
     * @param postId
     */
    public void modifyPost(String title, String content, Long userId, Long postId) {
        User user = userService.getUserEntityOrException(userId);
        Post post = getPostEntityOrException(postId);

        if (!post.getUser().equals(user)) {
            throw new ApplicationException(ErrorCode.INVALID_PERMISSION, String.format("userId[%s] has no permission with postId[%s]", userId, postId));
        }

        post.modify(title, content);

        postRepository.saveAndFlush(post);
    }

    /**
     * Delete a post
     *
     * @param postId
     * @param userId
     */
    public void deletePost(Long postId, Long userId) {
        User user = userService.getUserEntityOrException(userId);
        Post post = getPostEntityOrException(postId);

        if (!post.getUser().equals(user)) {
            throw new ApplicationException(ErrorCode.INVALID_PERMISSION, String.format("userId[%s] has no permission with postId[%s]", userId, postId));
        }

        post.delete();

        postRepository.saveAndFlush(post);
    }

    /**
     * Get posts
     *
     * @param pageable
     * @return
     */
    public Page<Post> getPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    /**
     * Get a post
     *
     * @param postId
     * @return
     */
    public Post getPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() ->
                new ApplicationException(ErrorCode.POST_NOT_FOUND, String.format("PostId[%s] not found", postId)));
    }

    // =================================================================================================================

    /**
     * Get a post entity or exception
     *
     * @param postId
     * @return
     */
    private Post getPostEntityOrException(Long postId) {
        return postRepository.findById(postId).orElseThrow(() ->
                new ApplicationException(ErrorCode.POST_NOT_FOUND, String.format("PostId[%s] not found", postId)));
    }
}
