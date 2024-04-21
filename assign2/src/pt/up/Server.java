package pt.up;

import java.net.*;
import java.io.*;

public class Server {

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: java Server <port>");
            System.exit(1);
        }
        System.out.println("Starting server");

        int portNumber = Integer.parseInt(args[0]);
        try (
                ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));) {
            while (true) {
                Socket clientSocket = serverSocket.accept();

                // Accept incoming connections
                // Start a service thread
                Thread.ofVirtual().start(
                        new ServerThread(clientSocket));
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
