import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class Play {
    private final int port;
    private final String hostname;
    private SSLSocket socket;

    /**
     * Constructor for the Play class.
     * 
     * @param hostname The hostname of the server.
     * @param port     The port of the server.
     */
    public Play(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    /**
     * Main method.
     * 
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        Play play = new Play("localhost", 8008);

        play.start();
    }

    /**
     * Starts the client.
     */
    public void start() {
      System.setProperty("javax.net.ssl.keyStore", "key_store.jks");
      System.setProperty("javax.net.ssl.keyStorePassword", "keystore");

        try {
            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            this.socket = (SSLSocket) factory.createSocket(this.hostname, this.port);

            System.out.println("Connected to server on port " + this.port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

