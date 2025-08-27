package com.thiscompany.ttrack.repository;

import com.thiscompany.ttrack.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, String>, JpaSpecificationExecutor<Task> {

    @EntityGraph(value = "task.graph" , type = EntityGraph.EntityGraphType.FETCH)
    Optional<Task> findById(String id);

    void deleteTaskById(String id);

    Optional<Task> findOne(Specification<Task> spec);

    @EntityGraph(value = "task.graph", type = EntityGraph.EntityGraphType.FETCH)
    @Override
    List<Task> findAll(Specification<Task> spec);

    @Override
    boolean exists(Specification<Task> spec);

    Page<Task> findAll(Specification<Task> spec, Pageable pageable);
}
