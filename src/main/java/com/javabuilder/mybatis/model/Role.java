package com.javabuilder.mybatis.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Role {
    private Long id;
    private String name;
    private String description;
}
