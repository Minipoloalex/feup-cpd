package pt.up;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class ClientConnection implements Runnable {
    private final Socket socket;
    private final GameManager gameManager;
    private final Database database;
    private Connection connection;

    public ClientConnection(Socket clientSocket, GameManager gameManager) throws IOException {
        this.socket = clientSocket;
        this.gameManager = gameManager;
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
                    return Message.ok();
                }

                case MessageType.LOGIN -> {
                    String username = data.get(0), password = data.get(1);

                    if (!database.checkUserPassword(username, password)) {
                        return Message.error("Invalid username or password");
                    }

                    String token = database.generateToken(username);
                    return Message.ok(token);
                }

                case MessageType.NORMAL -> {
                    String username = data.get(0), token = data.get(1);

                    if (!database.checkUserToken(username, token)) {
                        return Message.error("Invalid token");
                    }

                    gameManager.addNormalPlayer(new Player(username, token, connection));
                    return Message.ok();
                }

                case MessageType.LEAVE -> {
                    String username = data.get(0), token = data.get(1);

                    if (!database.checkUserToken(username, token)) {
                        return Message.error("Invalid token");
                    }

                    gameManager.removeNormalPlayer(new Player(username, token, connection));
                    return Message.ok();
                }

                default -> throw new AssertionError();
            }
        } catch (Exception e) {
            return Message.error("Error while processing request");
        }
    }

    private synchronized void checkForGames(Message request) throws InterruptedException {
        if (request.getType() == MessageType.NORMAL) {
            System.out.println("Players in q: " + gameManager.getNormalPlayers());
            System.out.println("Checking for games");
            gameManager.manage();
        }
    }

    @Override
    public void run() {
        try {
            connection = new Connection(socket);
            while (true) {
                Message request = connection.listen();
                System.out.println("Received request: " + request);
                Message answer = handle(request);
                connection.sendRequest(answer);
                System.out.println("Sent answer: " + answer);
                checkForGames(request);
            }
        } catch (InterruptedException e) {
            System.err.println("Exception caught when listening for a connection");
            System.err.println(e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Exception caught when closing the socket");
                System.err.println(e.getMessage());
            }
        }
    }
}
