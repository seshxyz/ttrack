package com.thiscompany.ttrack.core.executor.impl;

import com.thiscompany.ttrack.core.executor.CustomExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
public class SingleThreadExecutor implements CustomExecutor {
	
	@Qualifier("authTaskExecutorService")
	private final ExecutorService executor;
	
	private static final Logger LOG = LoggerFactory.getLogger(SingleThreadExecutor.class);
	
	public SingleThreadExecutor(@Qualifier("kafkaExecutorService") ExecutorService executor) {
		this.executor = executor;
	}
	
	@Override
	public void runAsync(Runnable task, Runnable onComplete) {
		CompletableFuture.runAsync(task, executor)
			.whenComplete((result, ex) -> {
				if(ex != null) {
					LOG.error("Async task caused error: {}\n\n{}", ex.getMessage(), ex.getCause());
				}
				onComplete.run();
			});
	}
	
	@Override
	public void runAsync(Runnable task) {
		CompletableFuture.runAsync(task, executor)
			.whenComplete((result, ex) -> {
				if(ex != null) {
					LOG.error("Async task caused error: {}\n\n{}", ex.getMessage(), ex.getCause());
				}
			});
	}
	
}
