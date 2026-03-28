package com.jt.project.controller;
import com.jt.project.dto.ProjectRequestDTO;
import com.jt.project.dto.ProjectResponseDTO;
import com.jt.project.entity.Project;
import com.jt.project.entity.ProjectStatus;
import com.jt.project.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    public ProjectResponseDTO mapToDTO(Project project){
        ProjectResponseDTO dto= new ProjectResponseDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setCreatedByUsername(project.getCreatedBy().getUsername());
        dto.setCreatedAt(project.getCreatedAt());
        dto.setStatus(project.getStatus().name());
        return dto;
    }

    // POST /api/projects

    @PostMapping
    public ResponseEntity<ProjectResponseDTO> createProject(@RequestBody ProjectRequestDTO request){
        Project project = projectService.createProject(request.getName(),request.getDescription(),request.getCreatedById());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDTO(project));
    }

    // GET /api/projects
    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects(){
        List<ProjectResponseDTO> projects = projectService.getAllProjects()
                .stream().map(this::mapToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(projects);
    }

    //  GET /api/projects/5
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable int id){
        Project project = projectService.getProjectById(id);
        return ResponseEntity.ok(mapToDTO(project));
    }

    // PUT /api/projects/5
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> updateProject(@PathVariable int id,@RequestBody ProjectRequestDTO request){
        ProjectStatus status = request.getStatus() != null ? ProjectStatus.valueOf(request.getStatus()) : null;
        Project updated = projectService.updateProject(id,request.getName(),request.getDescription(),status);
        return ResponseEntity.ok(mapToDTO(updated));
    }

    // DELETE /api/projects/5
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable int id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
