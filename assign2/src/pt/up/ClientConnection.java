package pt.up;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class ClientConnection implements Runnable {
    private final Socket clientSocket;
    private final Database database;

    public ClientConnection(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.database = new Database();
    }

    private Message handle(Message message) {
        ArrayList<String> data = message.getContentAsList();

        try {
            switch (message.getType()) {
                case MessageType.REGISTER -> {
                    String username = data.get(0), password = data.get(1);

                    if (!database.storeNewUser(username, password)) {
                        return Message.error("Username already exists");
                    }
                }

                case MessageType.LOGIN -> {
                    String username = data.get(0), password = data.get(1);

                    if (!database.checkUserPassword(username, password)) {
                        return Message.error("Invalid username or password");
                    }

                    String token = database.generateToken(username);
                    return Message.ok(token);
                }
                default -> throw new AssertionError();

                case MessageType.NORMAL -> {
                    String username = data.get(0), token = data.get(1);

                    if (!database.checkUserToken(username, token)) {
                        return Message.error("Invalid token");
                    }

                    return Message.ok("Confirm game (y/n):");
                }
            }
        } catch (Exception e) {
            return Message.error("Error while processing request");
        }

        // default response OK
        return Message.ok();
    }

    @Override
    public void run() {
        try (Connection connection = new Connection(clientSocket)) {
            while (true) {
                Message request = connection.listen();
                System.out.println("Received request: " + request);
                Message answer = handle(request);
                connection.sendRequest(answer);
                System.out.println("Sent answer: " + answer);
            }
        } catch (Exception e) {
            System.err.println("Exception caught when listening for a connection");
            System.err.println(e.getMessage());
        }
    }
}
