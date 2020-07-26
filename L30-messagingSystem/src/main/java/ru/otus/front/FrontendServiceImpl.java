package ru.otus.front;

import ru.otus.UserData;
import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageType;
import ru.otus.messagesystem.client.MsClient;
import java.util.function.Consumer;
import java.util.function.Function;


public class FrontendServiceImpl implements FrontendService {

    private final MsClient msClient;
    private final String databaseServiceClientName;

    public FrontendServiceImpl(MsClient msClient, String databaseServiceClientName) {
        this.msClient = msClient;
        this.databaseServiceClientName = databaseServiceClientName;
    }

    @Override
    public void getUserData(long userId, Consumer<String> dataConsumer) {
        Message<UserData> outMsg = msClient.produceMessage(databaseServiceClientName, new UserData(userId), MessageType.USER_DATA,
                getMessageCallback(dataConsumer));
        msClient.sendMessage(outMsg);
    }

    private MessageCallback<UserData> getMessageCallback(Consumer<String> dataConsumer) {
        return userData -> dataConsumer.accept(userData.getData());
    }
}
