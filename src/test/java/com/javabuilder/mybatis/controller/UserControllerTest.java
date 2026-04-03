package com.javabuilder.mybatis.controller;

import com.javabuilder.mybatis.common.UserStatus;
import com.javabuilder.mybatis.dto.request.CreateUserRequest;
import com.javabuilder.mybatis.dto.request.UpdateUserRequest;
import com.javabuilder.mybatis.dto.response.CreateUserResponse;
import com.javabuilder.mybatis.dto.response.PageResponse;
import com.javabuilder.mybatis.dto.response.UserResponse;
import com.javabuilder.mybatis.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test.properties")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @MockitoBean
    private UserService userService;

    private final String email = "duc@gmail.com";

    private CreateUserRequest createUserRequest;
    private CreateUserResponse createUserResponse;
    private UserResponse userResponse;
    private PageResponse<UserResponse> pageResponse;
    private UpdateUserRequest updateUserRequest;

    @BeforeEach
    void setUp() {
        createUserRequest = new CreateUserRequest(email, "123456", "duc");
        createUserResponse = new CreateUserResponse(1L, email, "duc", UserStatus.ACTIVE);
        userResponse = UserResponse.builder()
                .id(1L)
                .name("duc")
                .email(email)
                .status(UserStatus.ACTIVE)
                .build();

        pageResponse = PageResponse.<UserResponse>builder()
                .currentPage(1)
                .pageSize(5)
                .totalPages(1)
                .totalElements(1)
                .content(List.of(new UserResponse(1L, "duc", email, UserStatus.ACTIVE)))
                .build();

        updateUserRequest = new UpdateUserRequest("duc212", "duc212@gmail.com");
    }

    @Test
    void createUser_whenValidRequest_thenReturn201() throws Exception {
        // GIVEN
        when(userService.createUser(any(CreateUserRequest.class)))
                .thenReturn(createUserResponse);

        // WHEN, THEN
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(createUserRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(jsonMapper.writeValueAsString(createUserResponse)));
    }

    @Test
    void myInfo_whenAuthenticated_thenReturn200() throws Exception {
        when(userService.myInfo(1L)).thenReturn(userResponse);

        mockMvc.perform(get("/users/my-info")
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt().jwt(jwt -> jwt.subject("1")))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("duc"))
                .andExpect(jsonPath("$.email").value("duc@gmail.com"))
        ;
    }

    @Test
    void searchByEmail_whenValidEmail_thenReturn200() throws Exception {
        when(userService.findByEmail(1, 5, email)).thenReturn(pageResponse);

        mockMvc.perform(get("/users")
                .param("page", "1")
                .param("size", "5")
                .param("email", email)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getById_whenValidId_thenReturn200() throws Exception {
        when(userService.getUserById(1L)).thenReturn(userResponse);

        mockMvc.perform(get("/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(jsonMapper.writeValueAsString(userResponse)));
    }

    @Test
    void updateUser_whenValidRequest_thenReturn200() throws Exception {
        doNothing().when(userService).update(1L, updateUserRequest);

        mockMvc.perform(put("/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(updateUserRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
