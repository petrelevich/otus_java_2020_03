package ru.otus.front;

import java.util.function.Consumer;

public interface FrontendService {
    void getUserData(long userId, Consumer<String> dataConsumer);
}

