package pt.up;

public class User implements Comparable<User> {
    private final String username;
    private final String hashedPassword;
    private final String salt;
    private String token;
    private int rating;

    public User(String username, String password, String salt) {
        this(username, password, salt, 1000);
    }

    public User(String username, String password, String salt, int rating) {
        this.username = username;
        this.hashedPassword = password;
        this.salt = salt;
        this.rating = rating;
    }

    public synchronized String getHashedPassword() {
        return this.hashedPassword;
    }

    public synchronized String getSalt() {
        return this.salt;
    }

    public synchronized boolean checkPassword(String encryptedPassword) {
        return this.hashedPassword.equals(encryptedPassword);
    }

    public synchronized String getUsername() {
        return this.username;
    }

    public synchronized String toCSV() {
        return String.format("%s;%s;%s;%d", username, salt, hashedPassword, rating);
    }

    public synchronized int getRating() {
        return this.rating;
    }

    public synchronized void setRating(int rating) {
        this.rating = rating;
    }

    public synchronized void updateRating(int ratingDiff) {
        rating += ratingDiff;
    }

    public synchronized String getToken() {
        return this.token;
    }

    public synchronized void setToken(String token) {
        this.token = token;
    }

    @Override
    public synchronized int compareTo(User o) {
        int myRating = this.rating;
        int otherRating = o.getRating();

        if (myRating == otherRating) {
            // If ratings are equal, compare usernames to maintain a consistent ordering
            return this.username.compareTo(o.getUsername());
        } else {
            // Compare based on ratings
            return Integer.compare(myRating, otherRating);
        }
    }

}
