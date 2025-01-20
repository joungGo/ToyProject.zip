package org.example.exercisespringboot.user;

import lombok.Getter;

@Getter
// 사용자 권한
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private String value;

    UserRole(String value) {
        this.value = value;
    }
}