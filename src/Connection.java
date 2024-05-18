import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.net.ssl.SSLSocket;

public class Connection {   
    public static void send(SSLSocket socket, String message) throws IOException {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        out.write(message + "\n");
        out.flush();
    }

    public static String receive(SSLSocket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        return in.readLine();
    }
    
    public static void close(SSLSocket socket) throws IOException {
        socket.close();
    }
}
