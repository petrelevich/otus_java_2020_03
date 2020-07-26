package ru.otus.messagesystem;

import java.util.function.Consumer;

public interface MessageCallback<T> extends Consumer<T> {
}
