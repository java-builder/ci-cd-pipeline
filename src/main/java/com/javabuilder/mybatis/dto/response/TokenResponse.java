package com.javabuilder.mybatis.dto.response;

import lombok.Builder;

@Builder
public record TokenResponse(
        String token
) {
}
