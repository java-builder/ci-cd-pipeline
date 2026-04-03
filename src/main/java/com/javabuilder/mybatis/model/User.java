package com.javabuilder.mybatis.model;

import com.javabuilder.mybatis.common.UserStatus;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private UserStatus status;

    @Builder.Default
    private List<Role> roles = new ArrayList<>();

    public void addRole(Role role) {
        this.roles.add(role);
    }
}
