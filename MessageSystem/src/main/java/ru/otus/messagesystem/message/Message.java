package ru.otus.messagesystem.message;

import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.messagesystem.client.ResultDataType;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class Message<T extends ResultDataType> {

    private final MessageId id;
    private final String from;
    private final String to;
    private final MessageId sourceMessageId;
    private final String type;
    private final byte[] payload;
    private final Class<?> payloadClass;
    private final MessageCallback<T> callback;

    Message(MessageId messageId, String from, String to, MessageId sourceMessageId, String type, byte[] payload, Class<?> payloadClass, MessageCallback<T> callback) {
        this.id = messageId;
        this.from = from;
        this.to = to;
        this.sourceMessageId = sourceMessageId;
        this.type = type;
        this.payload = payload;
        this.payloadClass = payloadClass;
        this.callback = callback;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message<?> message = (Message<?>) o;
        return Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", sourceMessageId=" + sourceMessageId +
                ", type='" + type + '\'' +
                ", payload=" + Arrays.toString(payload) +
                ", callback=" + callback +
                '}';
    }

    public MessageId getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getType() {
        return type;
    }

    public byte[] getPayload() {
        return payload;
    }

    public Class<?> getPayloadClass() {
        return payloadClass;
    }

    public MessageCallback<T> getCallback() {
        return callback;
    }

    public Optional<MessageId> getSourceMessageId() {
        return Optional.ofNullable(sourceMessageId);
    }
}
