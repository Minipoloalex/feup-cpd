package pt.up.fe.cpd2324.server;

import pt.up.fe.cpd2324.client.Player;
import pt.up.fe.cpd2324.common.Connection;
import pt.up.fe.cpd2324.common.Utils;
import pt.up.fe.cpd2324.queue.NormalQueue;
import pt.up.fe.cpd2324.queue.RankedQueue;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class Server {
    private final int port;

    private SSLServerSocket serverSocket;
    
    private final Set<Player> authenticatedPlayers = new TreeSet<>();
    private final Set<Player> availablePlayers = new TreeSet<>();
    
    private final NormalQueue<Player> normalQueue = new NormalQueue<>();
    private final RankedQueue<Player> rankedQueue = new RankedQueue<>();

    private final ExecutorService normalPool = Executors.newVirtualThreadPerTaskExecutor();
    private final ExecutorService rankedPool = Executors.newVirtualThreadPerTaskExecutor();

    private long lastPing = System.currentTimeMillis();

    public Server(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        Server server = new Server(8080);

        try {
            server.start();
            server.run();
        } catch (IOException e) {
            System.out.println("Error starting the server: " + e.getMessage());
        } finally {
            try {
                server.stop();
            } catch (IOException e) {
                System.out.println("Error stopping the server: " + e.getMessage());
            }
        }
    } 

    public void start() throws IOException {
        // Set the system properties for the keystore (SSL)
        System.setProperty("javax.net.ssl.keyStore", "key_store.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "keystore");

        // Create the server socket
        SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        this.serverSocket = (SSLServerSocket) factory.createServerSocket(this.port);

        Utils.clearScreen();
        System.out.println("Server started on port " + this.port);
    }
    
    public void stop() throws IOException {
        this.serverSocket.close();
    }

    private void run() {
        try {
            // Start the queue manager
            Thread.ofVirtual().start(new QueueManager(this.availablePlayers, this.normalQueue, this.rankedQueue));
            
            // Start the game scheduler
            Thread.ofVirtual().start(new GameScheduler(this.normalQueue, this.rankedQueue, this.normalPool, this.rankedPool));

            // Start the ping thread
            // Ensures that clients are still alive
            Thread.ofVirtual().start(() -> {
                while (true) {
                    this.pingClients();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("Error pinging clients: " + e.getMessage());
                    }
                }
            });
            
            // Wait for clients to connect
            while (true) {
                SSLSocket clientSocket = (SSLSocket) this.serverSocket.accept();
                System.out.println("Client connected from " + clientSocket.getInetAddress().getHostAddress());
                
                // Start a new thread for the client authentication
                Thread.ofVirtual().start(new ClientAuthenticator(clientSocket, this.authenticatedPlayers, this.availablePlayers));
            }
        } catch (Exception e) {
            System.out.println("Error running the server: " + e.getMessage());
        }
    }

    private void pingClients() {
        if (System.currentTimeMillis() - this.lastPing < 5000) {   // Ping every 10 seconds
            return;
        }

        lastPing = System.currentTimeMillis();
    
        for (Player player : this.authenticatedPlayers) {      
            try {
                System.out.println("Pinging client " + player.getUsername());
                Connection.ping(player.getSocket());
            } catch (IOException e) {
                System.out.println("Error pinging client " + player.getUsername() + ": " + e.getMessage());
                this.disconnectPlayer(player);
            }
        }
    }
            
    private void disconnectPlayer(Player player)  {
        this.authenticatedPlayers.remove(player);
        this.availablePlayers.remove(player);
        this.normalQueue.remove(player);
        this.rankedQueue.remove(player);

        System.out.println("Player " + player.getUsername() + " disconnected");
    }
}
