package com.thiscompany.ttrack.model;

import com.thiscompany.ttrack.enums.Priority;
import com.thiscompany.ttrack.enums.TaskState;
import com.thiscompany.ttrack.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(length = 540)
    private String description;

    @Column(nullable = false)
    private Priority priority;

    @Column(nullable = false)
    private TaskStatus status;

    @Column(nullable = false)
    private TaskState state;

    @Column(nullable = false)
    private Boolean completed = false;

}
