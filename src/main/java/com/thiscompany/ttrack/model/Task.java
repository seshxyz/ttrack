package com.thiscompany.ttrack.model;

import com.thiscompany.ttrack.enums.Priority;
import com.thiscompany.ttrack.enums.TaskState;
import com.thiscompany.ttrack.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Priority priority;

    @Column(nullable = false)
    private TaskStatus status;

    @Column(nullable = false)
    private TaskState state;

    @Column
    private Boolean completed;

}
