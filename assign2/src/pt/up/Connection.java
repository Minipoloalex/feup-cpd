package pt.up;

import java.io.*;
import java.net.*;
import java.util.concurrent.locks.ReentrantLock;

/* This class is basically a Protocol Facade */

public class Connection implements AutoCloseable {
    private Protocol protocol;
    private String hostname;
    private int port;
    private final ReentrantLock lock = new ReentrantLock();

    public Connection(Socket socket) {
        this.protocol = new Protocol(socket);
    }

    public Connection(String hostname, int port) throws IOException {
        this(new Socket(hostname, port));
        this.hostname = hostname;
        this.port = port;
    }

    public Connection replicate() throws IOException {
        return new Connection(this.hostname, this.port);
    }

    /**
     * Sends a message to the server.
     * 
     * @param message the message to send
     */

    public void sendRequest(Message message) {
        lock.lock();
        try {
            protocol.sendRequest(message);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Receives a message from the server.
     * 
     * @return the message received from the server (or an error message if the
     *         server did not respond in time)
     */
    public Message receiveRequest() {
        lock.lock();
        try {
            return protocol.receiveRequest();
        } finally {
            lock.unlock();
        }
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
        lock.lock();
        try {
            return protocol.listen();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void close() {
        protocol.close();
    }

    public Socket getSocket() {
        return this.protocol.getSocket();
    }
}
