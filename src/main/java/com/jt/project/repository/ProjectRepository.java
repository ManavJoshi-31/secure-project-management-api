package com.jt.project.repository;

import com.jt.project.entity.Project;
import com.jt.project.entity.ProjectStatus;
import com.jt.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByCreatedBy(User createdBy);

    List<Project> findByStatus(ProjectStatus status);

    boolean existsByName(String name);
}