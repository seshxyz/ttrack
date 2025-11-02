package com.thiscompany.ttrack.core.executor;

public interface CustomExecutor {
	
	void runAsync(Runnable task, Runnable onComplete);
	
	void runAsync(Runnable task);
	
}
