package com.thiscompany.ttrack.service.user.post_authentication;

import com.thiscompany.ttrack.controller.user.dto.UserEntryTimestamp;
import com.thiscompany.ttrack.core.executor.CustomExecutor;
import com.thiscompany.ttrack.core.interfaces.ScheduledDecider;
import com.thiscompany.ttrack.service.database.DataWriter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class UserEntryWriteDecider implements ScheduledDecider<UserEntryTimestamp> {
	
	private static final Logger LOG = LoggerFactory.getLogger(UserEntryWriteDecider.class);
	
	private final DataWriter<List<UserEntryTimestamp>> datePersister;
	private final AuthenticationEventBufferOperator eventBuffer;
	private final CustomExecutor executor;
	private final AtomicBoolean isFlushing = new AtomicBoolean(false);
	private final Supplier<Instant> lastFlushTime = Instant::now;
	
	
	@Override
	public void tryExecute(UserEntryTimestamp info) {
		if(isFlushing.compareAndSet(false, true)) {
			doSave();
		}
	}
	
	@Scheduled(fixedDelay = 15, timeUnit = TimeUnit.SECONDS, initialDelay = 20)
	@Override
	public void scheduledTry() {
		if(isFlushing.compareAndSet(false, true)) {
			doSave();
		}
	}
	
	private void doSave() {
		executor.runAsync(()-> {
			try {
				datePersister.write(eventBuffer.transformCollection());
				LOG.debug("Last data flushed at: {}", lastFlushTime.get());
			} catch (DataIntegrityViolationException e) {
				LOG.warn("Exception raised while authentication data persisting: {}\n{}",e.getCause(), e.getMessage());
			} finally {
				isFlushing.set(false);
			}
		});
	}
	
}
