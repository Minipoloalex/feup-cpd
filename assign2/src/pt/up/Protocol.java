package pt.up;

import java.io.*;
import java.net.*;
import java.util.Optional;

public class Protocol implements AutoCloseable {
    private static final int TIMEOUT = 3000;
    private Socket socket;
    private OutputStream output;
    private PrintWriter writer;
    private InputStream input;

    public Protocol(Socket socket) {
        try {
            this.socket = socket;
            this.output = socket.getOutputStream();
            this.writer = new PrintWriter(output, true);
            this.input = socket.getInputStream();
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }

    public Protocol(String hostname, int port) throws IOException {
        this(new Socket(hostname, port));
    }

    public void sendRequest(Message message) {
        send(message);
    }

    public Message receiveRequest() {
        return receive().orElse(Message.error("Server did not respond."));
    }

    private void send(Message message) {
        writer.println(message);
    }

    private Optional<Message> receive() {
        try {
            socket.setSoTimeout(TIMEOUT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            return Optional.of(Message.parse(reader.readLine()));
        } catch (SocketTimeoutException ex) {
            System.out.println("Timeout: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public void close() {
        try {
            socket.close();
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}
