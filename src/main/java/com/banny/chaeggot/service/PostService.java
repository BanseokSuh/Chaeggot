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
    private final UserEntityRepository userEntityRepository;

    /**
     * Create post
     *
     * @param title
     * @param content
     * @return
     */
    public Long createPost(String title, String content, Long userIdx) {
        UserEntity userEntity = getUserEntityOrException(userIdx);

        PostEntity postEntity = postEntityRepository.save(PostEntity.of(title, content, userEntity));
        return postEntity.getPostIdx();
    }

    /**
     * Modify post
     *
     * @param title
     * @param content
     * @param userIdx
     * @param postIdx
     */
    public void modifyPost(String title, String content, Long userIdx, Long postIdx) {
        UserEntity userEntity = getUserEntityOrException(userIdx);
        PostEntity postEntity = getPostEntityOrException(postIdx);

        if (!postEntity.getUser().equals(userEntity)) {
            throw new ApplicationException(ErrorCode.INVALID_PERMISSION, String.format("UserIdx[%s] has no permission with UserIdx[%s]", userIdx, postIdx));
        }

        postEntity.modify(title, content);

        postEntityRepository.saveAndFlush(postEntity);
    }

    /**
     * Delete post
     *
     * @param userIdx
     * @param postIdx
     */
    public void deletePost(Long userIdx, Long postIdx) {
        UserEntity userEntity = getUserEntityOrException(userIdx);
        PostEntity postEntity = getPostEntityOrException(postIdx);

        if (!postEntity.getUser().equals(userEntity)) {
            throw new ApplicationException(ErrorCode.INVALID_PERMISSION, String.format("UserIdx[%s] has no permission with UserIdx[%s]", userIdx, postIdx));
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
     * Get post
     *
     * @param postIdx
     * @return
     */
    public Post getPost(Long postIdx) {
        return postEntityRepository.findById(postIdx).map(Post::fromEntity).orElseThrow(() ->
                new ApplicationException(ErrorCode.POST_NOT_FOUND, String.format("PostIdx[%s] not found", postIdx)));
    }

    // =================================================================================================================

    /**
     * Get user entity or exception
     *
     * @param userIdx
     * @return
     */
    public UserEntity getUserEntityOrException(Long userIdx) {
        return userEntityRepository.findByUserIdx(userIdx).orElseThrow(() ->
                new ApplicationException(ErrorCode.USER_NOT_FOUND, String.format("UserIdx[%s] not found", userIdx)));
    }

    /**
     * Get post entity or exception
     *
     * @param postIdx
     * @return
     */
    private PostEntity getPostEntityOrException(Long postIdx) {
        return postEntityRepository.findById(postIdx).orElseThrow(() ->
                new ApplicationException(ErrorCode.POST_NOT_FOUND, String.format("PostIdx[%s] not found", postIdx)));
    }
}
