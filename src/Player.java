import javax.net.ssl.SSLSocket;

public class Player implements Comparable<Player> {
    private final String username;
    private final String password;
    private final String salt;
    private int rating;

    private SSLSocket socket;

    /**
     * Constructor for the Player class.
     * 
     * @param username The username of the player.
     * @param password The password of the player.
     * @param salt The salt of the player.
     */
    public Player(String username, String password, String salt) {
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.rating = 1000;
    }

    /**
     * Constructor for the Player class.
     * 
     * @param username The username of the player.
     * @param password The password of the player.
     * @param salt The salt of the player.
     * @param rating The rating of the player.
     */
    public Player(String username, String password, String salt, int rating) {
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.rating = rating;
    }

    /**
     * Constructor for the Player class.
     * 
     * @param username The username of the player.
     * @param password The password of the player.
     * @param salt The salt of the player.
     * @param rating The rating of the player.
     */
    public Player(String username, String password, String salt, int rating, SSLSocket socket) {
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.rating = rating;
        this.socket = socket;
    }

    /**
     * Gets the username of the Player.
     * 
     * @return The username of the player.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Gets the password of the Player.
     * 
     * @return The password of the player.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Gets the salt of the Player.
     * 
     * @return The salt of the player.
     */
    public String getSalt() {
        return this.salt;
    }

    /**
     * Gets the rating of the Player.
     * 
     * @return The rating of the player.
     */
    public int getRating() {
        return this.rating;
    }

    /**
     * Sets the rating of the Player.
     * 
     * @param rating The rating of the player.
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Compares two players based on their rating.
     * 
     * @param other The other player to compare to.
     * @return A negative integer, zero, or a positive integer as this player is less than, equal to, or greater than the other player.
     */
    @Override
    public int compareTo(Player other) {
        if (this.rating != other.rating) {
            return Integer.compare(this.rating, other.rating);
        } else {
            return this.username.compareTo(other.username);
        }
    }

    /**
     * Gets the socket of the Player.
     * 
     * @return The socket of the player.
     */
    public SSLSocket getSocket() {
        return this.socket;
    }

    /**
     * Sets the socket of the Player.
     * 
     * @param socket The socket of the player.
     */
    public void setSocket(SSLSocket socket) {
        this.socket = socket;
    }
}
