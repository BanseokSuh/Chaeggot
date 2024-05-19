package com.banny.chaeggot.service;

import com.banny.chaeggot.controller.response.PostResponse;
import com.banny.chaeggot.exception.ApplicationException;
import com.banny.chaeggot.exception.ErrorCode;
import com.banny.chaeggot.model.Post;
import com.banny.chaeggot.model.entity.PostEntity;
import com.banny.chaeggot.model.entity.UserEntity;
import com.banny.chaeggot.repository.PostEntityRepository;
import com.banny.chaeggot.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserService userService;

    /**
     * Create a post
     *
     * @param title
     * @param content
     * @return
     */
    public Post createPost(String title, String content, Long id) {
        UserEntity userEntity = userService.getUserEntityOrException(id);

        PostEntity postEntity = postEntityRepository.save(PostEntity.of(title, content, userEntity));
        return Post.fromEntity(postEntity);
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
        UserEntity userEntity = userService.getUserEntityOrException(userId);
        PostEntity postEntity = getPostEntityOrException(postId);

        if (!postEntity.getUser().equals(userEntity)) {
            throw new ApplicationException(ErrorCode.INVALID_PERMISSION, String.format("userId[%s] has no permission with postId[%s]", userId, postId));
        }

        postEntity.modify(title, content);

        postEntityRepository.saveAndFlush(postEntity);
    }

    /**
     * Delete a post
     *
     * @param postId
     * @param userId
     */
    public void deletePost(Long postId, Long userId) {
        UserEntity userEntity = userService.getUserEntityOrException(userId);
        PostEntity postEntity = getPostEntityOrException(postId);

        if (!postEntity.getUser().equals(userEntity)) {
            throw new ApplicationException(ErrorCode.INVALID_PERMISSION, String.format("userId[%s] has no permission with postId[%s]", userId, postId));
        }

        postEntity.delete();

        postEntityRepository.saveAndFlush(postEntity);
    }

    /**
     * Get posts
     *
     * @param pageable
     * @return
     */
    public Page<Post> getPosts(Pageable pageable) {
        return postEntityRepository.findAll(pageable).map(Post::fromEntity);
    }

    /**
     * Get a post
     *
     * @param id
     * @return
     */
    public Post getPost(Long id) {
        return postEntityRepository.findById(id).map(Post::fromEntity).orElseThrow(() ->
                new ApplicationException(ErrorCode.POST_NOT_FOUND, String.format("PostIdx[%s] not found", id)));
    }

    // =================================================================================================================

    /**
     * Get a post entity or exception
     *
     * @param id
     * @return
     */
    private PostEntity getPostEntityOrException(Long id) {
        return postEntityRepository.findById(id).orElseThrow(() ->
                new ApplicationException(ErrorCode.POST_NOT_FOUND, String.format("PostIdx[%s] not found", id)));
    }
}
