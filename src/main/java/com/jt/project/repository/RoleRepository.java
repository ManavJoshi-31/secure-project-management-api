package com.jt.project.repository;
import com.jt.project.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    boolean existsByName(String name);
    Role findByRole(String name);
}
