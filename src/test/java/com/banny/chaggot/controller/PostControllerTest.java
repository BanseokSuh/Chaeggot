package com.banny.chaggot.controller;

import com.banny.chaggot.controller.request.PostCreateRequest;
import com.banny.chaggot.controller.request.PostModifyRequest;
import com.banny.chaggot.model.User;
import com.banny.chaggot.service.UserService;
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

import static com.banny.chaggot.util.JwtTokenUtils.generateToken;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.access-expired-time-ms}")
    private Long accessExpiredTimeMs;

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

        User user = userService.loadUserByUserId("admin");
        String token = generateToken(user, secretKey, accessExpiredTimeMs);

        mockMvc.perform(post("/api/v1/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 작성 실패 - 인증되지 않은 사용자")
    @Transactional
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
    @DisplayName("게시글 수정")
    @Transactional
    public void modifyPost() throws Exception {
        String title = "Modify_title_002";
        String body = "Modify_body_002";

        User user = userService.loadUserByUserId("admin");
        String token = generateToken(user, secretKey, accessExpiredTimeMs);

        mockMvc.perform(put("/api/v1/post/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 수정 실패 - 인증되지 않은 사용자")
    @Transactional
    public void modifyPostFail() throws Exception {
        String title = "Modify_title_002";
        String body = "Modify_body_002";

        mockMvc.perform(put("/api/v1/post/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("게시글 수정 실패 - 다른 사용자의 게시글 수정 시도")
    @Transactional
    public void modifyPostFail2() throws Exception {
        String title = "Modify_title_002";
        String body = "Modify_body_002";

        User user = userService.loadUserByUserId("admin");
        String token = generateToken(user, secretKey, accessExpiredTimeMs);

        mockMvc.perform(put("/api/v1/post/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body))))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

}
