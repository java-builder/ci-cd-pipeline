package com.javabuilder.mybatis.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CreateUserRequest(

        @NotBlank(message = "Email is required")
        @Email(message = "Email is not valid")
        String email,

        @NotBlank(message = "Password is required")
        @Length(min = 6, message = "Password must be at least 6 characters long")
        String password,

        @NotBlank(message = "Name is required")
        @Length(min = 2, message = "Name must be at least 2 characters long")
        String name
) {
}
