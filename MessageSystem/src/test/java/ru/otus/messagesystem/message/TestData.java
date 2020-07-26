package ru.otus.messagesystem.message;

import java.io.Serializable;
import java.util.Objects;

class TestData implements Serializable {
    int x;
    String str;
    int y;

    TestData(int x, String str, int y) {
        this.x = x;
        this.str = str;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestData testData = (TestData) o;
        return x == testData.x &&
                y == testData.y &&
                Objects.equals(str, testData.str);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, str, y);
    }
}
