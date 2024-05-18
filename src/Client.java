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
            if (this.authenticate()) {
                this.selectGameMode();
                this.playGame();
            }
        } catch (IOException e) {
            System.out.println("Error running the client: " + e.getMessage());
        }
    }

    private boolean authenticate() throws IOException {
        Utils.clearScreen();
        while (true) {
            String option = System.console().readLine(Connection.receive(this.socket));
            Connection.send(this.socket, option);
            String username = System.console().readLine(Connection.receive(this.socket));
            Connection.send(this.socket, username);
            String password = new String(System.console().readPassword(Connection.receive(this.socket)));
            Connection.send(this.socket, password);
            
            String response = Connection.receive(this.socket);

            if (response.equals("OK")) {
                Utils.clearScreen();
                System.out.println("Welcome, " + username + "!");
                return true;
            } else {
                System.out.println(response);
            }
        }
    }

    private void selectGameMode() throws IOException {
        while (true) {
            String option = System.console().readLine(Connection.receive(this.socket));
            Connection.send(this.socket, option);
            
            String response = Connection.receive(this.socket);

            if (response.equals("OK")) {
                Utils.clearScreen();
                System.out.println("Selected game mode: " + option);
                System.out.println("Waiting for another player to join...");
                return;
            } else {
                System.out.println(response);
            }
        }
    }

    private void playGame() throws IOException {
        while (true) {
            String message = Connection.receive(this.socket);
            
            if (message.startsWith("STONES")) {
                Utils.clearScreen();
                System.out.println(message);
            }
            else if (message.startsWith("Enter")) {
                String move = System.console().readLine(message);
                Connection.send(this.socket, move);
            } else if (message.equals("You won!") || message.equals("You lost!")) {
                Utils.clearScreen();
                System.out.println(message);
                break;
            } else {
                System.out.println(message);
            }
        }
    }
}
