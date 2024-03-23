package com.banny.springboota.service;

import com.banny.springboota.exception.ApplicationException;
import com.banny.springboota.exception.ErrorCode;
import com.banny.springboota.model.User;
import com.banny.springboota.model.entity.UserEntity;
import com.banny.springboota.repository.UserEntityRepository;
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
            throw new ApplicationException(ErrorCode.INVALID_USER_ID, "User ID should not be blank");
        }

        if (userId.length() < 5) {
            throw new ApplicationException(ErrorCode.INVALID_USER_ID, "User ID should be over 5 letters");
        }

        if (StringUtils.isBlank(userName)) {
            throw new ApplicationException(ErrorCode.INVALID_USER_NAME, "User name should not be blank");
        }

        if (StringUtils.isBlank(password)) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD, "Password should not be blank");
        }

        if (password.length() < 5) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD, "Password should be over 5 letters");
        }

        Pattern pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.find()) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD, "Password should include at least 1 special symbol");
        }
    }
}
