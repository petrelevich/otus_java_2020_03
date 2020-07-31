package ru.otus.protobuf;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import ru.otus.protobuf.generated.Empty;
import ru.otus.protobuf.generated.RemoteDBServiceGrpc;
import ru.otus.protobuf.generated.UserMessage;

import java.util.Iterator;

public class GRPCClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8090;

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        RemoteDBServiceGrpc.RemoteDBServiceBlockingStub stub = RemoteDBServiceGrpc.newBlockingStub(channel);
        UserMessage savedUserMsg = stub.saveUser(
                UserMessage.newBuilder().setFirstName("Вася").setLastName("Кириешкин").build()
        );

        System.out.printf("Мы сохранили Васю: {id: %d, name: %s %s}\n",
                savedUserMsg.getId(), savedUserMsg.getFirstName(), savedUserMsg.getLastName());

        Iterator<UserMessage> allUsersIterator = stub.findAllUsers(Empty.getDefaultInstance());
        System.out.println("Конградулейшенз! Мы получили юзеров! Среди них должен найтись один Вася!");
        allUsersIterator.forEachRemaining(um ->
                System.out.printf("{id: %d, name: %s %s}\n",
                        um.getId(), um.getFirstName(), um.getLastName())
        );

        channel.shutdown();
    }
}
