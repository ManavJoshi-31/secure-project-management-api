package com.jt.project.service;

import com.jt.project.entity.*;
import com.jt.project.exception.*;
import com.jt.project.repository.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class TaskService {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(ProjectRepository projectRepository,
                          TaskRepository taskRepository,
                          UserRepository userRepository){
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Task createTask(String title , String description,int projectId, int assignedToId){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()-> new ResourceNotFoundException("Project not found with id: "+projectId));

        if((project.getStatus()).equals(ProjectStatus.COMPLETED)){
                throw new BusinessRuleException("Cannot add tasks to Completed project.");
        }

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);

        User assignee = userRepository.findById(assignedToId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + assignedToId));
        task.setAssignedTo(assignee);
        task.setProject(project);

        return taskRepository.save(task);
    }

    public List<Task> getAllTasks(){
        return taskRepository.findAll();
    }

    public Task getTaskById(int id){
        return taskRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Task not found wiht id: "+id));
    }

    public List<Task> getTasksByProject(int projectId){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()-> new ResourceNotFoundException("Project not found with id: "+projectId));
        return taskRepository.findByProject(project);
    }

    public List<Task> getTasksByAssignedUser(int userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with id : "+userId));
        return taskRepository.findByAssignedTo(user);
    }

    public void deleteTask(int id){
        Task task = taskRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Task not found with id : "+id));
        taskRepository.delete(task);
    }

    public Task updateTask(int taskId, String title,
                           String description, TaskStatus status) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Task not found with id: " + taskId));

        String currentUsername = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        User currentUser = userRepository.findByUsername(currentUsername);

        boolean isEmployee = currentUser.getRole().getRole().equals("ROLE_EMPLOYEE");

        if (isEmployee) {
            if (task.getAssignedTo() == null || task.getAssignedTo().getId() != currentUser.getId()) {
                throw new BusinessRuleException("Employees can only update their own assigned tasks.");
            }
        }

        if (title != null) task.setTitle(title);
        if (description != null) task.setDescription(description);
        if (status != null) task.setStatus(status);

        return taskRepository.save(task);
    }
}
