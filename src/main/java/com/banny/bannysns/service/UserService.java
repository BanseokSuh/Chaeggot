package com.banny.bannysns.service;

import com.banny.bannysns.exception.ApplicationException;
import com.banny.bannysns.exception.ErrorCode;
import com.banny.bannysns.model.User;
import com.banny.bannysns.model.entity.UserEntity;
import com.banny.bannysns.repository.UserEntityRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final BCryptPasswordEncoder encoder;

    /**
     * 회원가입
     * @param userId
     * @param userName
     * @param password
     * @return User
     */
    @Transactional
    public User join(String userId, String userName, String password) {
        validateUserInfo(userId, userName, password);

        userEntityRepository.findByUserId(userId).ifPresent(it -> {
            throw new ApplicationException(ErrorCode.DUPLICATED_USER_ID, String.format("%s is duplicated", userId));
        });

        UserEntity userEntity = userEntityRepository.save(UserEntity.of(userId, userName, encoder.encode(password)));
        return User.fromEntity(userEntity);
    }

    private void validateUserInfo(String userId, String userName, String password) {
        if (StringUtils.isBlank(userId)) {
            throw new ApplicationException(ErrorCode.EMPTY_USER_ID, "User ID should not be empty");
        }

        if (userId.length() < 5) {
            throw new ApplicationException(ErrorCode.INVALID_USER_ID_LENGTH, "User ID is too short");
        }

        if (StringUtils.isBlank(userName)) {
            throw new ApplicationException(ErrorCode.EMPTY_USER_NAME, "User name should not be empty");
        }

        if (StringUtils.isBlank(password)) {
            throw new ApplicationException(ErrorCode.EMPTY_PASSWORD, "Password should not be empty");
        }

        if (password.length() < 5) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD_LENGTH, "Password is too short");
        }

        Pattern pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.find()) {
            throw new ApplicationException(ErrorCode.PASSWORD_NOT_MATCHED, "Password should include at least 1 special symbol");
        }
    }
}
