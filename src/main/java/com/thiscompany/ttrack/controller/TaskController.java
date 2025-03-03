package com.thiscompany.ttrack.controller;

import com.thiscompany.ttrack.controller.payload.TaskResponse;
import com.thiscompany.ttrack.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TaskController {

    TaskService taskService;

    @GetMapping
    public ResponseEntity<TaskResponse> findTaskById(@RequestParam Long id) {
       /*return ResponseEntity.ok(taskService.getTask(id));*/
        return new ResponseEntity<>(taskService.getTask(id), null, HttpStatus.FOUND);
    }



}
