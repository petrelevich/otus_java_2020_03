package ru.otus.messagesystem.message;

public class MessageHelper {
    public static <T> T getPayload(Message msg) {
        return (T) Serializers.deserialize(msg.getPayload());
    }

}
