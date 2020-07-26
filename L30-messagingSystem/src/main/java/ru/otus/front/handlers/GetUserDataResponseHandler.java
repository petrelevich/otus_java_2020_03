package ru.otus.front.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.UserData;
import ru.otus.messagesystem.message.MessageHelper;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.RequestHandler;

import java.util.Optional;

public class GetUserDataResponseHandler implements RequestHandler<UserData> {
    private static final Logger logger = LoggerFactory.getLogger(GetUserDataResponseHandler.class);

    @Override
    public Optional<Message<UserData>> handle(Message<UserData> msg) {
        logger.info("new message:{}", msg);
        try {
            msg.getCallback().accept(MessageHelper.getPayload(msg));
        } catch (Exception ex) {
            logger.error("msg:{}", msg, ex);
        }
        return Optional.empty();
    }
}
