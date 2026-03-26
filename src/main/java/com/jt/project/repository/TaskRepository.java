package com.jt.project.repository;

import com.jt.project.entity.Project;
import com.jt.project.entity.TaskStatus;
import com.jt.project.entity.Task;
import com.jt.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByProject(Project project);
    List<Task> findByAssignedTo(User assignedTo);
    List<Task> findByProjectAndStatus(Project project, TaskStatus status);

    boolean existsByProjectAndStatusNot(Project project, TaskStatus status);
}
