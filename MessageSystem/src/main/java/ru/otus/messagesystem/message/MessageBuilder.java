package ru.otus.messagesystem.message;

import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.messagesystem.client.ResultDataType;

import java.util.concurrent.atomic.AtomicLong;

public class MessageBuilder {
    private static final AtomicLong MESSAGE_ID_GENERATOR = new AtomicLong(0);
    private static final Message<ResultDataType> VOID_MESSAGE = new Message<>(getNextMessageId(),null, null, null, "voidTechnicalMessage", new byte[1], null,null);

    private MessageBuilder() {
    }

    public static Message<ResultDataType> getVoidMessage() {
        return VOID_MESSAGE;
    }

    public static <T extends ResultDataType> Message<T> buildMessage(String from, String to, MessageId sourceMessageId, T data, MessageType msgType, MessageCallback<T> callback) {
        return new Message<>(getNextMessageId(), from, to, sourceMessageId, msgType.getName(), Serializers.serialize(data), data.getClass(), callback);
    }

    private static MessageId getNextMessageId() {
        return new MessageId(MESSAGE_ID_GENERATOR.incrementAndGet());
    }
}
