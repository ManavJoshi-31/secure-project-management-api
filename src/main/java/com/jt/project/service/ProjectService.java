package com.jt.project.service;

import com.jt.project.entity.*;
import com.jt.project.exception.*;
import com.jt.project.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository,
                          TaskRepository taskRepository,
                          UserRepository userRepository){
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Project createProject(String name , String description , int createdById){
        //for unique name
        if(projectRepository.existsByName(name)){
            throw new BusinessRuleException("The project with name ' "+name+" ' already exists. ");
        }
        User creator = userRepository.findById(createdById)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with this id: " + createdById));
        Project project = new Project();
        project.setName(name);
        project.setDescription(description);
        project.setCreatedBy(creator);
        return projectRepository.save(project);
    }

    public List<Project> getAllProjects(){
        return projectRepository.findAll();
    }

    public Project getProjectById(int id){
        return projectRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Project not found with id: "+id));
    }

    public List<Project> getProjectsByUser(int userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return projectRepository.findByCreatedBy(user);
    }

    public Project updateProject(int id,String name,String description , ProjectStatus status){
        Project project = projectRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Project not found with id: "+id));

        if(name != null && !name.equals(project.getName())) {
            if(projectRepository.existsByName(name)){
                throw new BusinessRuleException("Project with name ' "+name+" ' already exists");
            }
            project.setName(name);
        }
        if(description!=null){
            project.setDescription(description);
        }
        if(status!=null){
            project.setStatus(status);
        }
        return projectRepository.save(project);
    }

    public void deleteProject(int id){

        Project project =  projectRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Project not found with id: "+id));
        boolean hasIncompleteTasks = taskRepository.existsByProjectAndStatusNot(project,TaskStatus.DONE);
        if(hasIncompleteTasks){
            throw new BusinessRuleException("Cannot delete project ' "+project.getName()+" ' because it has incomplete tasks.");
        }
        projectRepository.delete(project);
    }
}
