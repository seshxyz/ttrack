package com.thiscompany.ttrack.kafka.consumer;

import com.thiscompany.ttrack.controller.user.dto.UserAuthDate;
import com.thiscompany.ttrack.service.authentication.entry_logging.UserAuthenticationWriter;
import com.thiscompany.ttrack.utils.SingleCollectionContainer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class AuthenticationConsumer {

    private final UserAuthenticationWriter userAuthenticationWriter;
    private final ExecutorService kafkaExecutorService;

    private final static int BATCH_SIZE = 100;

    private static final Logger log = LoggerFactory.getLogger(AuthenticationConsumer.class);
    
    private final AtomicInteger counter = new AtomicInteger(0);
    private final AtomicBoolean isFlushing = new AtomicBoolean(false);
    private final Supplier<Instant> lastFlushTime = Instant::now;
    private final SingleCollectionContainer<List<UserAuthDate>> container = new SingleCollectionContainer<>(ArrayList::new);
    private final ConcurrentHashMap<String, LocalDateTime> eventMap = new ConcurrentHashMap<>();

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "${app.kafka.group-id}", info = "auth-event")
    public void consumeAuthEvent(UserAuthDate info) {
        eventMap.compute(info.username(), (key, oldValue) -> {
            if (oldValue == null) {
                counter.incrementAndGet();
            }
            return info.lastLogin();
        });
        log.info("User [{}] authenticated {}", info.username(), info.lastLogin());
        if(counter.get() >= BATCH_SIZE && isFlushing.compareAndSet(false, true)) {
            executeFlushing();
        }
    }
    
    private void executeFlushing() {
        List<UserAuthDate> dataSet = container.acquire();
        eventMap.forEach((key, value) -> dataSet.add(new UserAuthDate(key, value)));
        
        CompletableFuture.runAsync(()->{
            userAuthenticationWriter.flushDataToDatabase(dataSet, BATCH_SIZE);
        }, kafkaExecutorService).exceptionallyAsync((ex) -> {
            resetAtomicsAndReleaseContainer();
            log.error(ex.getMessage(), ex.getCause());
            return null;
        });
        eventMap.clear();
        resetAtomicsAndReleaseContainer();
        
        log.debug("Data flushed at: {}", lastFlushTime.get());
    }
    
    @Scheduled(fixedDelay = 15, timeUnit = TimeUnit.SECONDS, initialDelay = 20)
    public void tryFlushDataByTime() {
        if (!eventMap.isEmpty() && isFlushing.compareAndSet(false, true)) {
            executeFlushing();
        }
    }
    
    private void resetAtomicsAndReleaseContainer() {
        container.release(container.acquire());
        counter.set(0);
        isFlushing.set(false);
    }

}
