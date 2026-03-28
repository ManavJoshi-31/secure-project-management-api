package com.jt.project.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProjectResponseDTO {
    private int id;
    private String name;
    private String description;
    private String status;
    private LocalDateTime createdAt;
    private String createdByUsername;
}