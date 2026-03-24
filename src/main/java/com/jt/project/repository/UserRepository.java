package com.jt.project.repository;
import com.jt.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User,Integer> {
    boolean existsByUserName(String name);
}
