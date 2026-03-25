package com.jt.project;

import com.jt.project.entity.Role;
import com.jt.project.entity.User;
import com.jt.project.repository.RoleRepository;
import com.jt.project.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args){
        if(!roleRepository.existsByRole("ROLE_ADMIN"))
            roleRepository.save(new Role("ROLE_ADMIN"));

        if(!roleRepository.existsByRole("ROLE_MANAGER"))
            roleRepository.save(new Role("ROLE_MANAGER"));

        if(!roleRepository.existsByRole("ROLE_EMPLOYEE"))
            roleRepository.save(new Role("ROLE_EMPLOYEE"));

        Role adminRole = roleRepository.findByRole("ROLE_ADMIN");
        Role managerRole = roleRepository.findByRole("ROLE_MANAGER");
        Role employeeRole = roleRepository.findByRole("ROLE_EMPLOYEE");

        if(!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin123@gmail.com");
            admin.setRole(adminRole);
            userRepository.save(admin);
        }
        if(!userRepository.existsByUsername("manager1")) {
            User manager = new User();
            manager.setUsername("manager1");
            manager.setPassword(passwordEncoder.encode("manager123"));
            manager.setEmail("manager123@gmail.com");
            manager.setRole(adminRole);
            userRepository.save(manager);
        }
        if(!userRepository.existsByUsername("emp1")) {
            User employee = new User();
            employee.setUsername("emp1");
            employee.setPassword(passwordEncoder.encode("emp123"));
            employee.setEmail("emp123@gmail.com");
            employee.setRole(adminRole);
            userRepository.save(employee);
        }
        System.out.println(" === TEST DATA ADDED === ");
    }
}
