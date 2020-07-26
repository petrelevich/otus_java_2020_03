package ru.otus.front;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.MsClient;
import java.util.function.Consumer;


public class FrontendServiceImpl implements FrontendService {

    private final MsClient msClient;
    private final String databaseServiceClientName;

    public FrontendServiceImpl(MsClient msClient, String databaseServiceClientName) {
        this.msClient = msClient;
        this.databaseServiceClientName = databaseServiceClientName;
    }

    @Override
    public void getUserData(long userId, Consumer<String> dataConsumer) {
        Message outMsg = msClient.produceMessage(databaseServiceClientName, userId, MessageType.USER_DATA, dataConsumer::accept);
        msClient.sendMessage(outMsg);
    }
}
