package com.jt.project.controller;

import com.jt.project.dto.TaskRequestDTO;
import com.jt.project.dto.TaskResponseDTO;
import com.jt.project.entity.Task;
import com.jt.project.entity.TaskStatus;
import com.jt.project.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    private TaskResponseDTO mapToDTO(Task task) {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus().name());
        dto.setProjectName(task.getProject().getName());

        if (task.getAssignedTo() != null) {
            dto.setAssignedToUsername(task.getAssignedTo().getUsername());
        } else {
            dto.setAssignedToUsername("Unassigned");
        }
        return dto;
    }

    // POST /api/tasks
    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskRequestDTO request) {
        Task task = taskService.createTask(request.getTitle(),request.getDescription(),request.getProjectId(),request.getAssignedToId());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDTO(task));
    }

    // GET /api/tasks
    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        List<TaskResponseDTO> tasks = taskService.getAllTasks()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tasks);
    }

    // GET /api/tasks/id/3
    @GetMapping("/id/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById( @PathVariable int id) {
        Task task = taskService.getTaskById(id);
        return ResponseEntity.ok(mapToDTO(task));
    }

    // GET /api/tasks/project/2
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByProject( @PathVariable int projectId) {

        List<TaskResponseDTO> tasks = taskService.getTasksByProject(projectId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(tasks);
    }

    // GET /api/tasks/user/1
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByUser(@PathVariable int userId) {
        List<TaskResponseDTO> tasks = taskService.getTasksByAssignedUser(userId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tasks);
    }

    // PUT /api/tasks/3
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable int id,@RequestBody TaskRequestDTO request) {
        TaskStatus status = request.getStatus() != null ? TaskStatus.valueOf(request.getStatus()): null;
        Task updated = taskService.updateTask(id,request.getTitle(),request.getDescription(),status);
        return ResponseEntity.ok(mapToDTO(updated));
    }

    // DELETE /api/tasks/3
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable int id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

}