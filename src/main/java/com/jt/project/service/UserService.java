package com.jt.project.service;

import com.jt.project.entity.Role;
import com.jt.project.entity.User;
import com.jt.project.exception.BusinessRuleException;
import com.jt.project.exception.ResourceNotFoundException;
import com.jt.project.repository.RoleRepository;
import com.jt.project.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public User registerUser(String username, String password,
                             String email, String roleName) {

        // Rule 1: username must be unique
        if (userRepository.existsByUsername(username)) {
            throw new BusinessRuleException("Username '" + username + "' is already taken.");
        }

        // Rule 2: role must exist
        Role role = roleRepository.findByRole(roleName);
        if (role == null) {
            throw new ResourceNotFoundException("Role not found: " + roleName +". Valid roles: ROLE_ADMIN, ROLE_MANAGER, ROLE_EMPLOYEE");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole(role);

        return userRepository.save(user);
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + id));
    }

    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException(
                    "User not found with username: " + username);
        }
        return user;
    }

    // Change password
    public User changePassword(int id, String oldPassword, String newPassword) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + id));

        // Rule: old password must match
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessRuleException("Old password is incorrect.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }
    public void deleteUser(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        userRepository.delete(user);
    }
}