package com.javabuilder.mybatis.dto.response;

import com.javabuilder.mybatis.common.UserStatus;
import lombok.Builder;

@Builder
public record UserResponse(
        Long id,
        String name,
        String email,
        UserStatus status
) {
}
