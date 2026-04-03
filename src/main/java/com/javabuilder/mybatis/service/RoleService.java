package com.javabuilder.mybatis.service;

import com.javabuilder.mybatis.mapper.RoleMapper;
import com.javabuilder.mybatis.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleMapper roleMapper;

    public Role findOrCreateRole(String name) {
        return roleMapper.findByName(name).orElseGet(() -> {
                    Role newRole = Role.builder()
                            .name(name)
                            .description(name)
                            .build();
                    roleMapper.insert(newRole);
                    return newRole;
                });
    }
}
