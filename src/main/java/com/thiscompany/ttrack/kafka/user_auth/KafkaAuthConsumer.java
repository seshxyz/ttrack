package com.thiscompany.ttrack.kafka.user_auth;

import com.thiscompany.ttrack.controller.user.dto.UserAuthDate;
import com.thiscompany.ttrack.service.user.auth_utils.SingleObjectContainer;
import com.thiscompany.ttrack.service.user.auth_utils.UserAuthDataWriter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class KafkaAuthConsumer {

    private final UserAuthDataWriter userAuthDataWriter;

    private final static int BATCH_SIZE = 20;

    private static final Logger log = LoggerFactory.getLogger(KafkaAuthConsumer.class);

    private final AtomicInteger counter = new AtomicInteger(0);
    private final AtomicBoolean isFlushing = new AtomicBoolean(false);
    private final Supplier<Instant> lastFlushTime = Instant::now;
    private final SingleObjectContainer<List<UserAuthDate>> container = new SingleObjectContainer<>(ArrayList::new);
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
        try {
            eventMap.forEach((key, value) -> dataSet.add(new UserAuthDate(key, value)));

            userAuthDataWriter.flushDataToDatabase(dataSet, BATCH_SIZE);

            eventMap.clear();
            counter.set(0);
            container.release(dataSet);
            isFlushing.set(false);
        } catch (DataAccessException e) {
            counter.set(0);
            isFlushing.set(false);
            container.release(dataSet);
            log.error(e.getMessage(), e.getCause());
        }
        log.debug("Data flushed at: {}", lastFlushTime.get());
    }


    @Scheduled(fixedDelay = 8, timeUnit = TimeUnit.SECONDS, initialDelay = 20)
    public void tryFlushDataByTime() {
        if(!eventMap.isEmpty() && isFlushing.compareAndSet(false, true)) {
            executeFlushing();
        }
    }

}
