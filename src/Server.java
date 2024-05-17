import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class Server {
    private final int port;
    private SSLServerSocket serverSocket;

    private final Database database = Database.getInstance();
    
    private final List<Thread> clientThreads = new ArrayList<>();
    private final ExecutorService normalPool = Executors.newVirtualThreadPerTaskExecutor();
    private final ExecutorService rankedPool = Executors.newVirtualThreadPerTaskExecutor();

    /**
     * Constructor for the Server class.
     * 
     * @param port The port of the server.
     */
    public Server(int port) {
        this.port = port;
    }

    /**
     * Main method.
     * 
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        Server server = new Server(8008);

        server.start();
    } 

    /**
     * Starts the server.
     */
    public void start() {
        System.setProperty("javax.net.ssl.keyStore", "key_store.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "keystore");

        try {
            SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            this.serverSocket = (SSLServerSocket) factory.createServerSocket(this.port);

            System.out.println("Server is now running on port " + this.port);

            while (true) {
                SSLSocket clientSocket = (SSLSocket) this.serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                // ClientHandler clientHandler = new ClientHandler(clientSocket, this.database, this.normalPool, this.rankedPool);
                // Thread clientThread = Thread.ofVirtual().start(clientHandler);
                // this.clientThreads.add(clientThread);
            }
        } catch (Exception e) {
            e.printStackTrace();

            try {
                this.serverSocket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            for (Thread clientThread : this.clientThreads) {
                clientThread.interrupt();
            }

            this.normalPool.close();
            this.rankedPool.close();

            System.exit(1);
        }      
    }
}
