package ru.otus.messagesystem;

public interface MsClient {

    boolean sendMessage(Message msg);

    void handle(Message msg);

    String getName();

    <T> Message produceMessage(String to, Object data, MessageType msgType, MessageCallback<T> callback);

}
