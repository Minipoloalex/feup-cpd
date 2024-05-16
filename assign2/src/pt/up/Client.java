package pt.up;

import java.io.*;
import java.net.*;
import pt.up.states.*;

public class Client {
    private State state;

    public Client(Connection connection) {
        this.state = new AuthMenuState(connection);
    }

    public static void main(String[] args) {
        System.out.println("Starting client");

        String hostname = "localhost";
        int port = 8000;

        try (Connection connection = new Connection(hostname, port)) {
            Client client = new Client(connection);
            while (true) {
                client.state.render();

                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String inputLine = reader.readLine();

                client.state = client.state.handle(inputLine);

                if (client.state == null) {
                    break;
                }
            }
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}
