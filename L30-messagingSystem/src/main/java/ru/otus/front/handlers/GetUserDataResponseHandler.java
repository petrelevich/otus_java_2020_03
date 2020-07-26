package ru.otus.front.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.app.common.Serializers;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.RequestHandler;

import java.util.Optional;

public class GetUserDataResponseHandler implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetUserDataResponseHandler.class);

    @Override
    public Optional<Message> handle(Message msg) {
        logger.info("new message:{}", msg);
        try {
            String userData = Serializers.deserialize(msg.getPayload(), String.class);
            msg.getCallback().accept(userData);
        } catch (Exception ex) {
            logger.error("msg:{}", msg, ex);
        }
        return Optional.empty();
    }
}
