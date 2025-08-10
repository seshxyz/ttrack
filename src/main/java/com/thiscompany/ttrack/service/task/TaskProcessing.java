package com.thiscompany.ttrack.service.task;

import org.springframework.http.ResponseEntity;

public interface TaskProcessing {

    ResponseEntity<?> promoteTask(String id, String requestUser);

    ResponseEntity<?> cancelTask(String id, String requestUser);

}
