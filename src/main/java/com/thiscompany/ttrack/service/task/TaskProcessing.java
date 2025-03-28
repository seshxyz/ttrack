package com.thiscompany.ttrack.service.task;

import org.springframework.http.ResponseEntity;

public interface TaskProcessing {

    ResponseEntity<?> promoteTask(Long id);

    ResponseEntity<?> cancelTask(Long id);

}
