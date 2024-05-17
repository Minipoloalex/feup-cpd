import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.net.ssl.SSLSocket;

public class Connection {   
    /**
     * Sends a message to the other end of the socket.
     * 
     * @param socket  The socket to send the message to.
     * @param message The message to send.
     * @throws IOException If an I/O error occurs.
     */
    public static void send(SSLSocket socket, String message) throws IOException {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        out.write(message + "\n");
        out.flush();
    }


    /**
     * Receives a message from the other end of the socket.
     * 
     * @param socket The socket to receive the message from.
     * @return The message received.
     * @throws IOException If an I/O error occurs.
     */
    public static String receive(SSLSocket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        return in.readLine();
    }
    
    /**
     * Closes the socket.
     * 
     * @param socket The socket to close.
     */
    public static void close(SSLSocket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
