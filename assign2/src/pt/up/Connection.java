package pt.up;

import java.io.*;
import java.net.*;

/* This class is basically a Protocol Facade */

public class Connection implements AutoCloseable {
    private Protocol protocol;

    public Connection(Socket socket) {
        this.protocol = new Protocol(socket);
    }

    public Connection(String hostname, int port) throws IOException {
        this(new Socket(hostname, port));
    }

    /**
     * Sends a message to the server.
     * 
     * @param message the message to send
     */

    public void sendRequest(Message message) {
        protocol.sendRequest(message);
    }

    /**
     * Receives a message from the server.
     * 
     * @return the message received from the server (or an error message if the
     *         server did not respond in time)
     */
    public Message receiveRequest() {
        return protocol.receiveRequest();
    }

    public Message register(String username, String password) {
        return protocol.register(username, password);
    }

    public Message login(String username, String password) {
        return protocol.login(username, password);
    }

    public Message playNormalGame(String username, String token) {
        return protocol.playNormalGame(username, token);
    }

    public Message listen() {
        return protocol.listen();
    }

    @Override
    public void close() {
        protocol.close();
    }
}
