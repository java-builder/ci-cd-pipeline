package com.javabuilder.mybatis.service;

import com.javabuilder.mybatis.mapper.UserRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRoleService {

    private final UserRoleMapper userRoleMapper;

    public void addRoleToUser(Long userId, Long roleId) {
        userRoleMapper.insert(userId, roleId);
    }
}
