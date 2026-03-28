package com.jt.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectRequestDTO {
    private String name;
    private String description;
    private int createdById;
    private String status;
}