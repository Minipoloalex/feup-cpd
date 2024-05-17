import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class Client {
    private final int port;
    private final String hostname;
    private SSLSocket socket;

    /**
     * Constructor for the Client class.
     * 
     * @param hostname The hostname of the server.
     * @param port     The port of the server.
     */
    public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    /**
     * Main method.
     * 
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        Client client = new Client("localhost", 8008);

        client.start();
        client.run();
    }

    /**
     * Starts the client.
     */
    private void start() {
        System.setProperty("javax.net.ssl.trustStore", "key_store.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "keystore");

        try {
            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            this.socket = (SSLSocket) factory.createSocket(this.hostname, this.port);

            System.out.println("Connected to server on port " + this.port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Runs the client.
     */
    private void run() {
        this.authenticate();

        while (true) {
            try {
                String message = Connection.receive(this.socket);
                System.out.println("Received message: " + message);
            } catch (Exception e) {
                e.printStackTrace();
                this.stop();
            }
        }
    }

    /**
     * Authenticates the client.
     * 
     * @return True if the client is authenticated, false otherwise.
     */
    private boolean authenticate() {
        while (true) {
            try {
                String option = System.console().readLine("Do you want to login or register? (login/register): ");
                String username = System.console().readLine("Enter your username: ");
                String password = new String(System.console().readPassword("Enter your password: "));

                Connection.send(this.socket, option + ";" + username + ";" + password);
                
                String response = Connection.receive(this.socket);

                if (response.equals("Success")) {
                    System.out.println("Authenticated successfully!");
                    return true;
                } else {
                    System.out.println(response);
                }
            } catch (Exception e) {
                e.printStackTrace();
                this.stop();
            }
        }
    }

    /**
     * Stops the client.
     */
    private void stop() {
        try {
            Connection.close(this.socket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        System.exit(0);
    }
}
