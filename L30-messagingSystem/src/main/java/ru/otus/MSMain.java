package ru.otus;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.db.handlers.GetUserDataRequestHandler;
import ru.otus.front.FrontendService;
import ru.otus.front.FrontendServiceImpl;
import ru.otus.db.DBServiceImpl;
import ru.otus.front.handlers.GetUserDataResponseHandler;
import ru.otus.messagesystem.*;

import java.util.EnumMap;
import java.util.Map;

public class MSMain {
    private static final Logger logger = LoggerFactory.getLogger(MSMain.class);

    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    public static void main(String[] args) throws InterruptedException {
        MessageSystem messageSystem = new MessageSystemImpl();

        Map<MessageType, RequestHandler> requestHandlerDatabase = new EnumMap<>(MessageType.class);
        requestHandlerDatabase.put(MessageType.USER_DATA, new GetUserDataRequestHandler(new DBServiceImpl()));
        new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME, messageSystem, requestHandlerDatabase);

        Map<MessageType, RequestHandler> requestHandlerFrontend = new EnumMap<>(MessageType.class);
        requestHandlerFrontend.put(MessageType.USER_DATA, new GetUserDataResponseHandler());

        MsClientImpl frontendMsClient = new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME, messageSystem, requestHandlerFrontend);
        FrontendService frontendService = new FrontendServiceImpl(frontendMsClient, DATABASE_SERVICE_CLIENT_NAME);

        frontendService.getUserData(1, data -> logger.info("got data:{}", data));

        Thread.sleep(100);
        messageSystem.dispose();
        logger.info("done");
    }
}
