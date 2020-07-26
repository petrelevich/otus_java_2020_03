package ru.otus.messagesystem.message;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SerializersTest {

    @Test
    void serializeDeSerialize() {
        TestData testData = new TestData(1, "str", 2);

        byte[] data = Serializers.serialize(testData);

        TestData object = (TestData) Serializers.deserialize(data);
        assertThat(object).isEqualTo(testData);
    }
}
