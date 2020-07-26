package ru.otus.messagesystem.message;

import java.util.Objects;

public class MessageId {
    private final long id;

    public MessageId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "MessageId{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageId messageId = (MessageId) o;
        return id == messageId.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
