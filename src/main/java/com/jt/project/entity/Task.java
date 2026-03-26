package com.jt.project.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
@Entity
@Table (name = "tasks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String description;
    private LocalDate dueDate;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id" , nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to" )
    private User assignedTo;

    @PrePersist
    protected void onCreate(){
        if(status == null)
            status = TaskStatus.TODO;
    }

}
