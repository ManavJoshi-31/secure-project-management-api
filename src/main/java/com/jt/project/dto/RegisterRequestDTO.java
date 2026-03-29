package com.jt.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDTO {
    private String username;
    private String password;
    private String email;
    private String roleName; // "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_EMPLOYEE"
}