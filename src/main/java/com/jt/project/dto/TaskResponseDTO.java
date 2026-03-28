package com.jt.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskResponseDTO {
    private int id;
    private String title;
    private String description;
    private String status;
    private String projectName;
    private String assignedToUsername;
}