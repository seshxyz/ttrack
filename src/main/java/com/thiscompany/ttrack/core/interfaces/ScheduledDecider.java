package com.thiscompany.ttrack.core.interfaces;

public interface ScheduledDecider<T> extends Decider<T> {
	
	void scheduledTry();
	
}
