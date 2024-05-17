import javax.net.ssl.SSLSocket;

public class Client implements Comparable<Client> {
    private final String username;
    private final String password;
    private final String salt;
    private int rating;

    private SSLSocket socket;

    /**
     * Constructor for the Client class.
     * 
     * @param username The username of the client.
     * @param password The password of the client.
     * @param salt The salt of the client.
     */
    public Client(String username, String password, String salt) {
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.rating = 1000;
    }

    /**
     * Constructor for the Client class.
     * 
     * @param username The username of the client.
     * @param password The password of the client.
     * @param salt The salt of the client.
     * @param rating The rating of the client.
     */
    public Client(String username, String password, String salt, int rating) {
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.rating = rating;
    }

    /**
     * Constructor for the Client class.
     * 
     * @param username The username of the client.
     * @param password The password of the client.
     * @param salt The salt of the client.
     * @param rating The rating of the client.
     */
    public Client(String username, String password, String salt, int rating, SSLSocket socket) {
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.rating = rating;
        this.socket = socket;
    }

    /**
     * Gets the username of the client.
     * 
     * @return The username of the client.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Gets the password of the client.
     * 
     * @return The password of the client.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Gets the salt of the client.
     * 
     * @return The salt of the client.
     */
    public String getSalt() {
        return this.salt;
    }

    /**
     * Gets the rating of the client.
     * 
     * @return The rating of the client.
     */
    public int getRating() {
        return this.rating;
    }

    /**
     * Sets the rating of the client.
     * 
     * @param rating The rating of the client.
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public int compareTo(Client other) {
        return Integer.compare(this.rating, other.rating);
    }

    /**
     * Gets the socket of the client.
     * 
     * @return The socket of the client.
     */
    public SSLSocket getSocket() {
        return this.socket;
    }

    /**
     * Sets the socket of the client.
     * 
     * @param socket The socket of the client.
     */
    public void setSocket(SSLSocket socket) {
        this.socket = socket;
    }
}
