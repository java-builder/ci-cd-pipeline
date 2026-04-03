package com.javabuilder.mybatis.dto.response;

import com.javabuilder.mybatis.common.UserStatus;
import lombok.Builder;

@Builder
public record CreateUserResponse(
        Long id,
        String email,
        String name,
        UserStatus status
) {
}
