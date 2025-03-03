package com.thiscompany.ttrack;

import com.thiscompany.ttrack.controller.payload.TaskResponse;
import com.thiscompany.ttrack.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<TaskResponse> findTaskById(Long id);

}
