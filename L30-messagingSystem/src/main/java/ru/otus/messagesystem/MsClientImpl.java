package ru.otus.messagesystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.app.common.Serializers;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class MsClientImpl implements MsClient {
    private static final Logger logger = LoggerFactory.getLogger(MsClientImpl.class);

    private final String name;
    private final MessageSystem messageSystem;
    private final Map<String, RequestHandler> handlers = new ConcurrentHashMap<>();

    public MsClientImpl(String name, MessageSystem messageSystem, Map<MessageType, RequestHandler> requestHandlerMap) {
        this.name = name;
        this.messageSystem = messageSystem;
        requestHandlerMap.forEach(this::addHandler);
        messageSystem.addClient(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean sendMessage(Message msg) {
        boolean result = messageSystem.newMessage(msg);
        if (!result) {
            logger.error("the last message was rejected: {}", msg);
        }
        return result;
    }

    @Override
    public void handle(Message msg) {
        logger.info("new message:{}", msg);
        try {
            RequestHandler requestHandler = handlers.get(msg.getType());
            if (requestHandler != null) {
                requestHandler.handle(msg).ifPresent(this::sendMessage);
            } else {
                logger.error("handler not found for the message type:{}", msg.getType());
            }
        } catch (Exception ex) {
            logger.error("msg:{}", msg, ex);
        }
    }

    @Override
    public <T> Message produceMessage(String to, Object data, MessageType msgType, MessageCallback<T> callback) {
        return new Message(name, to, null, msgType.getValue(), Serializers.serialize(data), callback);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MsClientImpl msClient = (MsClientImpl) o;
        return Objects.equals(name, msClient.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    private void addHandler(MessageType type, RequestHandler requestHandler) {
        handlers.put(type.getValue(), requestHandler);
    }
}
