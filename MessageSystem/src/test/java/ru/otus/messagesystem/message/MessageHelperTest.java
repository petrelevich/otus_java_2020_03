package ru.otus.messagesystem.message;

import org.junit.jupiter.api.Test;
import ru.otus.messagesystem.client.CallbackId;

import static org.assertj.core.api.Assertions.assertThat;

class MessageHelperTest {

    @Test
    void serializeDeSerializeMessage() {
        Message msgExpected = new Message(new MessageId(1), "from", "to", new MessageId(2), "type",
                new byte[12], new CallbackId(3));

        byte[] data = MessageHelper.serializeMessage(msgExpected);

        Message msg = MessageHelper.deSerializeMessage(data);

        assertThat(msg.getId()).isEqualTo(msgExpected.getId());
        assertThat(msg.getFrom()).isEqualTo(msgExpected.getFrom());
        assertThat(msg.getTo()).isEqualTo(msgExpected.getTo());
        assertThat(msg.getSourceMessageId()).isEqualTo(msgExpected.getSourceMessageId());
        assertThat(msg.getType()).isEqualTo(msgExpected.getType());
        assertThat(msg.getPayload()).isEqualTo(msgExpected.getPayload());
        assertThat(msg.getCallbackId()).isEqualTo(msgExpected.getCallbackId());
    }
}