package pt.up;

import java.io.*;
import java.net.*;

public class Server {
    private final ServerSocket serverSocket;
    private final GameManager gameManager;

    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.gameManager = new GameManager();
    }

    public void start() {
        // Initialize games
        Thread.ofVirtual().start(this.gameManager);

        // Initialize authentications
        // Thread.ofVirtual().start(this.authManager);
    }

    public static void main(String[] args) {
        System.out.println("Starting server");

        try {
            Server server = new Server(8000);
            server.start();

            while (true) {
                Socket clientSocket = server.serverSocket.accept();
                Thread.ofVirtual().start(new ClientConnection(clientSocket));
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen for a connection");
            System.out.println(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("FINALLY");
            Auth.saveUsers();
        }
    }
}
