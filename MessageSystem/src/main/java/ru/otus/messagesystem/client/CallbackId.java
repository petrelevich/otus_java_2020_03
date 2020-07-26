package ru.otus.messagesystem.client;

import java.io.Serializable;
import java.util.Objects;

public class CallbackId implements Serializable {
    private static final long serialVersionUID = 1L;
    private final long id;

    public CallbackId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "CallbackId{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CallbackId that = (CallbackId) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
