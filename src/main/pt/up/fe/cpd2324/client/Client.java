package pt.up.fe.cpd2324.client;

import pt.up.fe.cpd2324.common.Connection;
import pt.up.fe.cpd2324.common.Message;
import pt.up.fe.cpd2324.common.Utils;

import java.io.IOException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class Client {
    private final int port;
    private final String hostname;
    private SSLSocket socket;

    public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 8080);

        try {
            client.start();
            client.run();
        } catch (IOException e) {
            System.out.println("Error starting the client: " + e.getMessage());
        } finally {
            try {
                client.stop();
            } catch (IOException e) {
                System.out.println("Error stopping the client: " + e.getMessage());
            }
        }
    }

    private void start() throws IOException {
        // Set the system properties for the keystore (SSL)
        System.setProperty("javax.net.ssl.trustStore", "key_store.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "keystore");

        // Create the client socket
        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        this.socket = (SSLSocket) factory.createSocket(this.hostname, this.port);

        System.out.println("Connected to server on port " + this.port);
    }

    private void stop() throws IOException {
        this.socket.close();
    }

    private void run() {
        try {
            if (this.authenticate()) {  // Authenticate the user, if successful, start listening
                this.listen();
            } else {
                Utils.clearScreen();
                System.out.println("Exiting...");
                this.stop();
            }
        } catch (IOException e) {
            System.out.println("Error running the client: " + e.getMessage());
        }
    }

    private boolean authenticate() throws IOException {
        Utils.clearScreen();

        while (true) {
            Message message = Connection.receive(this.socket);
            String content = message.getContent();

            switch (message.getType()) {
                case SHOW:
                    System.out.println(content);
                    break;
                case PROMPT:
                    System.out.println();
                    Connection.send(this.socket, System.console().readLine(content));
                    break;
                case USERNAME:
                    Utils.clearScreen();
                    Connection.send(this.socket, System.console().readLine(content));
                    break;
                case PASSWORD:
                    Connection.send(this.socket, new String(System.console().readPassword(content)));
                    break;
                case OK:
                    Utils.clearScreen();
                    System.out.println(content);
                    return true;
                case ERROR:
                    Utils.clearScreen();
                    System.out.println(content);
                    break;
                default:
                    System.out.println("Invalid message type: " + message.getType());
                    break;
            }
        }
    }

    // Listen for messages from the server and handle them
    private void listen() throws IOException {
        while (true) {
            Message message = Connection.receive(this.socket);

            switch (message.getType()) {    // 2 possible states: Game mode selection or playing the game
                case MODE:
                    this.selectGameMode();
                    break;
                case GAME:
                    this.playGame();
                    break;
                default:
                    System.out.println("Invalid message type: " + message.getType());
                    break;
            }
        }
    }

    private void selectGameMode() throws IOException {
        while (true) { 
            Message message = Connection.receive(this.socket);
            String content = message.getContent();

            switch (message.getType()) {
                case SHOW:
                    System.out.println(content);
                    break;
                case PROMPT: 
                    System.out.println();
                    Connection.send(this.socket, System.console().readLine(content));
                    break;
                case OK:
                    Utils.clearScreen();
                    System.out.println(content);
                    System.out.println(Connection.receive(this.socket).getContent());
                    return;
                case ERROR:
                    Utils.clearScreen();
                    System.out.println(content);
                    break;
                default:
                    System.out.println("Invalid message type: " + message.getType());
            }
        }
    }
            

    private void playGame() throws IOException {
        while (true) {
            Message message = Connection.receive(this.socket);
            String content = message.getContent();

            switch (message.getType()) {
                case SHOW:
                    System.out.println(content);
                    break;
                case PROMPT:
                    System.out.println();
                    Connection.send(this.socket, System.console().readLine(content));
                    break;
                case CLEAR:
                    Utils.clearScreen();
                    break;
                case WAIT:
                    System.out.println();
                    System.out.println(content);
                    break;  
                case GAME_OVER:
                    Utils.clearScreen();
                    System.out.println(content);
                    System.out.println(Connection.receive(this.socket).getContent());
                    return;
                case ERROR:
                    Utils.clearScreen();
                    System.out.println(content);
                    break;
                default:
                    System.out.println("Invalid message type: " + message.getType());
            }
        }
    }
}
