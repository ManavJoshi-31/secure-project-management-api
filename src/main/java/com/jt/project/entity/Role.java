package com.jt.project.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true , nullable = false)
    private String role; // "ROLE_ADMIN" here role is prefix as SpringSecurity has in built ADMIN so to differentiate we are adding prefix as ROLE.

    public Role(String role) {
        this.role = role;
    }
}
