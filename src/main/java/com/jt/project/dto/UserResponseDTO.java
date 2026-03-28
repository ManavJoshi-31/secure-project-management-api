package com.jt.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {
    private int id;
    private String username;
    private String email;
    private String role;
}