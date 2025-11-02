package com.thiscompany.ttrack.core.interfaces;

public interface Decider<T> {
	
	void tryExecute(T data);
	
}
