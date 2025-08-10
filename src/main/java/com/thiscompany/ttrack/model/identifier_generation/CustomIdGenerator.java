package com.thiscompany.ttrack.model.identifier_generation;

import com.thiscompany.ttrack.model.Task;
import com.thiscompany.ttrack.model.User;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class CustomIdGenerator implements IdentifierGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor ssci, Object o) {
        return generateIdentifier(o);
    }

    private String generateIdentifier(Object of) {
        String prefix = switch (of) {
            case User u -> u.generateId();
            case Task t -> t.generateId();
            default -> "";
        };
        long leastBits = ThreadLocalRandom.current().nextLong();
        long mostBits = ThreadLocalRandom.current().nextLong();
        String uuid = new UUID(mostBits,leastBits).toString().substring(8);
        return prefix+uuid;
    }

}
