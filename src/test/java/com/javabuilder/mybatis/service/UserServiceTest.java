package com.javabuilder.mybatis.service;

import com.javabuilder.mybatis.common.RoleName;
import com.javabuilder.mybatis.common.UserStatus;
import com.javabuilder.mybatis.converter.UserConverter;
import com.javabuilder.mybatis.dto.request.CreateUserRequest;
import com.javabuilder.mybatis.dto.response.CreateUserResponse;
import com.javabuilder.mybatis.dto.response.UserResponse;
import com.javabuilder.mybatis.exception.ApplicationException;
import com.javabuilder.mybatis.mapper.UserMapper;
import com.javabuilder.mybatis.model.Role;
import com.javabuilder.mybatis.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private RoleService roleService;

    @Mock
    private UserRoleService userRoleService;

    @Mock
    private UserConverter userConverter;

    @InjectMocks
    private UserService userService;

    private CreateUserRequest request;
    private CreateUserResponse response;
    private User user;
    private Role role;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        request = new CreateUserRequest("duc@gmail.com", "123456", "duc");
        response = new CreateUserResponse(1L, "duc@gmail.com", "duc", UserStatus.ACTIVE);
        user = User.builder()
                .id(1L)
                .name("duc")
                .email("duc@gmail.com")
                .password("123456")
                .status(UserStatus.ACTIVE)
                .build();

        role = Role.builder()
                .id(1L)
                .name(RoleName.STUDENT.name())
                .description(RoleName.STUDENT.name())
                .build();

        userResponse = UserResponse.builder()
                .id(1L)
                .name("duc")
                .email("duc@gmail.com")
                .status(UserStatus.ACTIVE)
                .build();
    }

    @Test
    void createUser_whenValidRequest_thenReturnCreatedUser() {
        when(userMapper.existsByEmail(anyString())).thenReturn(false);
        when(userConverter.toUser(request)).thenReturn(user);
        when(roleService.findOrCreateRole(RoleName.STUDENT.name())).thenReturn(role);
        when(userConverter.toCreateUserResponse(user)).thenReturn(response);

        CreateUserResponse result = userService.createUser(request);

        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo(response.email());
        assertThat(result.name()).isEqualTo(response.name());
        assertThat(result.status()).isEqualTo(UserStatus.ACTIVE);

        verify(userMapper). insert(user);
        verify(userRoleService).addRoleToUser(user.getId(), role.getId());
    }

    @Test
    void createUser_whenUserAlreadyExists_thenReturnConflict() {
        when(userMapper.existsByEmail(anyString())).thenReturn(true);

        ApplicationException exception = assertThrows(
                ApplicationException.class,  // Kỳ vọng exception này được throw
                () -> userService.createUser(request) // Khi chạy dòng này
        );

        assertThat(exception.getErrorCode().getCode()).isEqualTo(400);
        assertThat(exception.getErrorCode().getMessage()).isEqualTo("User already exists");

        verify(userMapper, never()).insert(user);
        verify(userRoleService, never()).addRoleToUser(anyLong(), anyLong());
    }

    @Test
//    @WithMockUser(username = "1", roles = "STUDENT")
    void myInfo_whenValidUserId_thenReturnUserInfo() {
        when(userMapper.findById(1L)).thenReturn(userResponse);

        UserResponse result = userService.myInfo(1L);
        assertNotNull(result);

        assertThat(result.email()).isEqualTo(userResponse.email());
        assertThat(result.name()).isEqualTo(userResponse.name());
        assertThat(result.status()).isEqualTo(userResponse.status());
    }
}

