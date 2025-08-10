package com.thiscompany.ttrack.kafka.user_auth;

import com.thiscompany.ttrack.controller.user.dto.UserAuthDate;
import com.thiscompany.ttrack.service.user.auth_utils.SingleObjectContainer;
import com.thiscompany.ttrack.service.user.auth_utils.UserAuthDataWriter;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final UserAuthDataWriter userAuthDataWriter;

    private final static int BATCH_SIZE = 20;

    private static final Logger log = LogManager.getLogger(KafkaConsumer.class);

    private final AtomicInteger counter = new AtomicInteger(0);
    private final AtomicBoolean isFlushing = new AtomicBoolean(false);
    private final Supplier<Instant> lastFlushTime = Instant::now;
    private final SingleObjectContainer<UserAuthDate, List<UserAuthDate>> container = new SingleObjectContainer<>(ArrayList::new);
    private final ConcurrentSkipListMap<String, LocalDateTime> eventMap = new ConcurrentSkipListMap<>(String::compareTo);

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "${app.kafka.group-id}", info = "auth-event", batch = "true")
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
        eventMap.clear();

        userAuthDataWriter.flushDataToDatabase(dataSet, BATCH_SIZE);

        counter.set(0);
        container.release(dataSet);
        isFlushing.set(false);

        log.info("Data flushed at: {}", lastFlushTime.get());
    }


    @Scheduled(fixedDelay = 8, timeUnit = TimeUnit.SECONDS, initialDelay = 20)
    public void tryFlushDataByTime() {
        if(eventMap.isEmpty()) {
            return;
        }
        else if(isFlushing.compareAndSet(false, true)) {
            executeFlushing();
        }
    }

}
