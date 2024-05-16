package pt.up;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        System.out.println("Starting client");

        String hostname = "localhost";
        int port = 8000;

        try (Connection connection = new Connection(hostname, port)) {
            while (true) {
                connection.sendRequest(Message.ok());
                Message response = connection.receiveRequest();

                if (response.isError()) {
                    System.out.println("Error: " + response.getContent());
                } else {
                    System.out.println("Server response: " + response);
                }

                while (true) {
                }
            }
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}
