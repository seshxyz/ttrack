package com.thiscompany.ttrack.service.user.post_authentication;

import com.thiscompany.ttrack.controller.user.dto.UserEntryTimestamp;
import com.thiscompany.ttrack.core.SingleCollectionContainer;
import com.thiscompany.ttrack.core._abstract.MapCollector;
import com.thiscompany.ttrack.core.interfaces.BufferOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class AuthenticationEventBufferOperator implements BufferOperator<UserEntryTimestamp, List<UserEntryTimestamp>> {
	
	private static final Logger LOG = LoggerFactory.getLogger(AuthenticationEventBufferOperator.class);
	
	private final int collectorCapacity = 300;
	
	private final MapCollector<String, LocalDateTime> eventCollector =
		new MapCollector<>(()-> new HashMap<>(collectorCapacity), collectorCapacity);
	
	private final SingleCollectionContainer<List<UserEntryTimestamp>> bufferedCollection =
		new SingleCollectionContainer<>(()-> new ArrayList<>(collectorCapacity));
	
	private final ReentrantReadWriteLock operationLock = new ReentrantReadWriteLock();
	
	@Override
	public boolean addToBuffer(UserEntryTimestamp event) {
		operationLock.readLock().lock();
		try {
			eventCollector.addToBuffer(event.username(), (key, oldValue) -> {
				if (oldValue == null) {
					eventCollector.incrementCounter();
				}
				return event.lastLogin();
			});
			LOG.info("User [{}] authenticated {}", event.username(), event.lastLogin());
			
			return this.isCollectionFull();
			
		} finally {
			operationLock.readLock().unlock();
		}
	}
	
	@Override
	public List<UserEntryTimestamp> transformCollection() {
		operationLock.writeLock().lock();
		try {
			List<UserEntryTimestamp> eventsSnapshot = bufferedCollection.getOrCreate();
			eventCollector.forEach(
				(key, value) -> eventsSnapshot.add(new UserEntryTimestamp(key, value))
			);
			eventCollector.clearMap();
			return eventsSnapshot;
		} finally {
			operationLock.writeLock().unlock();
		}
	}
	
	@Override
	public boolean isCollectionFull() {
		return eventCollector.checkIsMapFull();
	}
	
}
