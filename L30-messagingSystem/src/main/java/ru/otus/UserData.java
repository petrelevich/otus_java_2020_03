package ru.otus;

import ru.otus.messagesystem.client.ResultDataType;

public class UserData extends ResultDataType {
    private final long userId;
    private final String data;

    public UserData(long userId) {
        this.userId = userId;
        this.data = null;
    }

    public UserData(long userId, String data) {
        this.userId = userId;
        this.data = data;
    }

    public long getUserId() {
        return userId;
    }

    public String getData() {
        return data;
    }
}
