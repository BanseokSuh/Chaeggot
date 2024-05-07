package com.banny.springboot.service;

import com.banny.springboot.exception.ApplicationException;
import com.banny.springboot.exception.ErrorCode;
import com.banny.springboot.model.User;
import com.banny.springboot.model.entity.UserEntity;
import com.banny.springboot.repository.UserCacheRepository;
import com.banny.springboot.repository.UserEntityRepository;
import com.banny.springboot.util.JwtTokenUtils;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final UserCacheRepository userCacheRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.access-expired-time-ms}")
    private Long accessExpiredTimeMs;

    /**
     * Join
     *
     * @param userId
     * @param userName
     * @param password
     * @return User
     */
    @Transactional
    public User join(String userId, String userName, String password) {
        /**
         * Valid check for user info
         */
        validateUserInfo(userId, userName, password);

        /**
         * Check duplicated user id
         */
        userEntityRepository.findByUserId(userId).ifPresent(it -> {
            throw new ApplicationException(ErrorCode.DUPLICATED_USER_ID, String.format("User ID %s is duplicated", userId));
        });

        /**
         * Save user info
         */
        UserEntity userEntity = userEntityRepository.save(UserEntity.of(userId, userName, encoder.encode(password)));
        return User.fromEntity(userEntity);
    }

    /**
     * Login
     *
     * @param userId
     * @param password
     * @return token
     */
    public String login(String userId, String password) {
        // Get user info
        User user = loadUserByUserId(userId);

        // Check password if it is correct
        if (!encoder.matches(password, user.getPassword())) {
            throw new ApplicationException(ErrorCode.WRONG_PASSWORD, "Password is wrong");
        }

        // Generate token with secret key and expired time
        String accessToken = JwtTokenUtils.generateToken(user, secretKey, accessExpiredTimeMs);

        // Cache user info
        userCacheRepository.setUser(user);

        return accessToken;
    }


    /**
     * Load user by userId.
     * - Get user from cache first.
     * - If not exist, get user from DB using .orElseGet() method.
     * - If not exist, throw exception.
     *
     * @param userId
     * @return User
     */
    public User loadUserByUserId(String userId) {
        return userCacheRepository.getUser(userId).orElseGet(() ->
                userEntityRepository.findByUserId(userId).map(User::fromEntity).orElseThrow(() ->
                        new ApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", userId)))
        );
    }

    /**
     * Valid check for user info
     * todo: Move this method to a separate class
     *
     * @param userId
     * @param userName
     * @param password
     */
    private void validateUserInfo(String userId, String userName, String password) {
        if (StringUtils.isBlank(userId)) {
            throw new ApplicationException(ErrorCode.EMPTY_USER_ID, String.format("User ID %s should not be empty", userId));
        }

        if (userId.length() < 5) {
            throw new ApplicationException(ErrorCode.INVALID_USER_ID, String.format("User ID %s is too short", userId));
        }

        if (StringUtils.isBlank(userName)) {
            throw new ApplicationException(ErrorCode.EMPTY_USER_NAME, String.format("Username %s should not be empty", userName));
        }

        if (StringUtils.isBlank(password)) {
            throw new ApplicationException(ErrorCode.EMPTY_PASSWORD, String.format("Password %s should not be empty", password));
        }

        if (password.length() < 5) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD, String.format("Password %s is invalid", password));
        }

        Pattern pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.find()) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD, String.format("Password %s should include at least 1 special symbol", password));
        }
    }
}
