package ru.otus.messagesystem.client;

import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageType;

public interface MsClient {

    boolean sendMessage(Message<? extends ResultDataType> msg);

    void handle(Message<? extends ResultDataType> msg);

    String getName();

    <T extends ResultDataType> Message<T> produceMessage(String to, T data, MessageType msgType, MessageCallback<T> callback);
}
