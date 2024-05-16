package pt.up;

import java.net.*;

public class ClientConnection implements Runnable {
    private final Socket clientSocket;

    public ClientConnection(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (Connection connection = new Connection(clientSocket)) {
            while (true) {
                Message message = connection.receiveRequest();
                System.out.println("Received message: " + message);
                connection.sendRequest(Message.ok());
                System.out.println("Sent message: " + Message.ok());
                while (true) {
                }
            }
        } catch (Exception e) {
            System.err.println("Exception caught when listening for a connection");
            System.err.println(e.getMessage());
        }
    }
}
