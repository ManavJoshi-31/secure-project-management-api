package com.jt.project.repository;

import com.jt.project.entity.Project;
import com.jt.project.entity.ProjectStatus;
import com.jt.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    /* Manual Implementation using JPQL
    @Query("SELECT p FROM Project p WHERE p.createdBy = :createdBy")
    List<Project> getProjectsByUser(@Param("createdBy") User createdBy);
     */

    /* Manual Implementation using EntityManager
        @PersistenceContext
        private EntityManager entityManager;
        public List<Project> findProjectsByUser(User user) {
        String jpql = "SELECT p FROM Project p WHERE p.createdBy = :user";
        return entityManager.createQuery(jpql, Project.class)
                .setParameter("user", user)
                .getResultList();
        }
     */
    List<Project> findByCreatedBy(User createdBy);

    List<Project> findByStatus(ProjectStatus status);

    boolean existsByName(String name);
}