package com.javabuilder.mybatis.converter;

import com.javabuilder.mybatis.common.UserStatus;
import com.javabuilder.mybatis.dto.request.CreateUserRequest;
import com.javabuilder.mybatis.dto.response.CreateUserResponse;
import com.javabuilder.mybatis.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConverter {

    private final PasswordEncoder passwordEncoder;

    public User toUser(CreateUserRequest request) {
        return User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .status(UserStatus.ACTIVE)
                .build();
    }

    public CreateUserResponse toCreateUserResponse(User user) {
        return CreateUserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .status(UserStatus.ACTIVE)
                .build();
    }

}
