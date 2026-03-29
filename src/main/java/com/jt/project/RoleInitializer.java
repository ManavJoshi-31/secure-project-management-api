package com.jt.project;

import com.jt.project.entity.Role;
import com.jt.project.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {

        if (!roleRepository.existsByRole("ROLE_ADMIN")) {
            roleRepository.save(new Role("ROLE_ADMIN"));
        }
        if (!roleRepository.existsByRole("ROLE_MANAGER")) {
            roleRepository.save(new Role("ROLE_MANAGER"));
        }
        if (!roleRepository.existsByRole("ROLE_EMPLOYEE")) {
            roleRepository.save(new Role("ROLE_EMPLOYEE"));
        }

        System.out.println("=== Roles initialized ===");
    }
}