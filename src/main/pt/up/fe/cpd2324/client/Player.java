package pt.up.fe.cpd2324.client;

import pt.up.fe.cpd2324.queue.Rateable;

import javax.net.ssl.SSLSocket;

public class Player implements Comparable<Player>, Rateable {
    private final String username;
    private final String password;
    private final String salt;
    private int rating;

    private SSLSocket socket;

    public Player(String username, String password, String salt) {
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.rating = 1000;
    }

    public Player(String username, String password, String salt, int rating) {
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.rating = rating;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getSalt() {
        return this.salt;
    }

    public int getRating() {
        return this.rating;
    }

    public SSLSocket getSocket() {
        return this.socket;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setSocket(SSLSocket socket) {
        this.socket = socket;
    }

    @Override
    public int compareTo(Player other) {
        if (this.rating != other.rating) {
            return Integer.compare(this.rating, other.rating);
        } else {
            return this.username.compareTo(other.username);
        }
    }
}
