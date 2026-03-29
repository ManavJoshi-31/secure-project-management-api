package com.jt.project.controller;

import com.jt.project.dto.RegisterRequestDTO;
import com.jt.project.dto.UserResponseDTO;
import com.jt.project.entity.User;
import com.jt.project.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    private UserResponseDTO mapToDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().getRole());
        return dto;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register( @RequestBody RegisterRequestDTO request) {
        User user = userService.registerUser(
                request.getUsername(),
                request.getPassword(),
                request.getEmail(),
                request.getRoleName()
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapToDTO(user));
    }

}