package com.jt.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequestDTO {
    private String title;
    private String description;
    private int projectId;
    private int assignedToId;
    private String status;
}