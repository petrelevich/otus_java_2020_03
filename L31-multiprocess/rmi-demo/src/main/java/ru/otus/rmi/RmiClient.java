package ru.otus.rmi;

import java.rmi.Naming;

public class RmiClient {
    public static void main(String[] args) throws Exception {

        EchoInterface echoInterface = (EchoInterface) Naming.lookup("//localhost/EchoServer");
        var dataFromServer = echoInterface.echo("hello");
        System.out.println(String.format("response from the server: %s", dataFromServer));

    }
}
