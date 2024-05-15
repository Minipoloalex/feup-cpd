package pt.up;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java Client <hostname> <port>");
            System.exit(1);
        }
        System.out.println("Starting client");

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);

        try (Socket socket = new Socket(hostname, port)) {
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            BufferedReader stdinReader = new BufferedReader(new InputStreamReader(System.in));
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            while (true) {
                String response;
                while ((response = reader.readLine()) != null && !response.isEmpty()) {
                    System.out.println(response);
                }

                String expression = stdinReader.readLine();
                writer.println(expression);
                /*
                while ((expression = stdinReader.readLine()) != null && !expression.isEmpty()) {
                    writer.println(expression);
                }
                writer.println();
                 */
            }
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}
