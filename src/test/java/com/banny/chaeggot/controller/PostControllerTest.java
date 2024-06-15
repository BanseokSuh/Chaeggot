package com.banny.chaeggot.controller;

import com.banny.chaeggot.controller.request.PostCreateRequest;
import com.banny.chaeggot.controller.request.PostModifyRequest;
import com.banny.chaeggot.entity.User;
import com.banny.chaeggot.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.banny.chaeggot.util.JwtTokenUtils.generateToken;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.access-expired-time-ms}")
    private Long accessTokenExpiredTimeMs;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    // todo: refactor this test
    @Test
    @Transactional
    // @WithMockUser(username = "admin", roles = {"USER"})
    @DisplayName("게시글 작성")
    public void createPost() throws Exception {
        String title = "Test_title_001";
        String body = "Test_body_001";

        User user = userService.loadUserByLoginId("admin");
        String token = generateToken(user, secretKey, accessTokenExpiredTimeMs);

        mockMvc.perform(post("/api/v1/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @DisplayName("게시글 작성 실패 - 인증되지 않은 사용자")
    public void createPostFail() throws Exception {
        String title = "Test_title_001";
        String body = "Test_body_001";

        mockMvc.perform(post("/api/v1/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    @DisplayName("게시글 수정")
    public void modifyPost() throws Exception {
        String title = "Modify_title_002";
        String body = "Modify_body_002";

        User user = userService.loadUserByLoginId("admin");
        String token = generateToken(user, secretKey, accessTokenExpiredTimeMs);

        mockMvc.perform(put("/api/v1/post/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @DisplayName("게시글 수정 실패 - 게시글 존재하지 않음")
    public void modifyPostFailPostNotExist() throws Exception {
        String title = "Modify_title_002";
        String body = "Modify_body_002";

        User user = userService.loadUserByLoginId("admin");
        String token = generateToken(user, secretKey, accessTokenExpiredTimeMs);

        mockMvc.perform(put("/api/v1/post/10000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body))))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @DisplayName("게시글 수정 실패 - 인증되지 않은 사용자")
    public void modifyPostFailUnauthorizedUser() throws Exception {
        String title = "Modify_title_002";
        String body = "Modify_body_002";

        mockMvc.perform(put("/api/v1/post/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    @DisplayName("게시글 수정 실패 - 다른 사용자의 게시글 수정 시도")
    public void modifyPostFailNotPermitted() throws Exception {
        String title = "Modify_title_002";
        String body = "Modify_body_002";

        User user = userService.loadUserByLoginId("admin");
        String token = generateToken(user, secretKey, accessTokenExpiredTimeMs);

        mockMvc.perform(put("/api/v1/post/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body))))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @DisplayName("게시글 삭제")
    public void deletePost() throws Exception {

        User user = userService.loadUserByLoginId("admin");
        String token = generateToken(user, secretKey, accessTokenExpiredTimeMs);

        mockMvc.perform(delete("/api/v1/post/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @DisplayName("게시글 삭제 실패 - 게시글 존재하지 않음")
    public void deletePostFailPostNotExist() throws Exception {

        User user = userService.loadUserByLoginId("admin");
        String token = generateToken(user, secretKey, accessTokenExpiredTimeMs);

        mockMvc.perform(delete("/api/v1/post/10000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @DisplayName("게시글 삭제 실패 - 인증되지 않은 사용자")
    public void deletePostFailUnauthorizedUser() throws Exception {

        mockMvc.perform(delete("/api/v1/post/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    @DisplayName("게시글 살제 실패 - 다른 사용자의 게시글 삭제 시도")
    public void deletePostFailNotPermittedUser() throws Exception {
        User user = userService.loadUserByLoginId("admin");
        String token = generateToken(user, secretKey, accessTokenExpiredTimeMs);

        mockMvc.perform(delete("/api/v1/post/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andDo(print());
    }
}
