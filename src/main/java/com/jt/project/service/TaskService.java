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

        return taskRepository.save(task);
    }

    public List<Task> getAllTasks(){
        return taskRepository.findAll();
    }

    public Task getTaskbyId(int id){
        return taskRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Task not found wiht id: "+id));
    }

    public List<Task> getTaskByProject(int projectId){
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
}
