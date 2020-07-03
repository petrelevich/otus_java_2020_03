package ru.otus.generators;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class UserIdGeneratorImpl implements UserIdGenerator {

    private static AtomicLong USER_ID = new AtomicLong(0);

    @Override
    public long getUserId() {
        return USER_ID.incrementAndGet();
    }
}
