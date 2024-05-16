package pt.up;

public class User implements Comparable<User> {
    
    private final String username;
    private String password;
    private String salt;
    private int rating;
    private String token;

    /**
     * Constructor for the User class.
     * 
     * @param username The username of the user.
     * @param password The password of the user.
     */
    public User(String username, String password) {
        
        this.username = username;
        this.password = password;
        this.rating = 1000;
    }

    /**
     * Constructor for the User class.
     * 
     * @param username The username of the user.
     * @param password The password of the user.
     * @param rating The rating of the user.
     */
    public User(String username, String password, int rating) {
        
        this.username = username;
        this.password = password;
        this.rating = rating;
    }

    /**
     * Constructor for the User class.
     * 
     * @param username The username of the user.
     * @param password The password of the user.
     * @param salt The salt of the user.
     * @param rating The rating of the user.
     */
    public User(String username, String password, String salt, int rating) {
        
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.rating = rating;
    }
    
    /**
     * Gets the user's username.
     * 
     * @return The username of the user.
     */
    public String getUsername() {
        
        return this.username;
    }

    /**
     * Gets the user's password.
     * 
     * @return The password of the user.
     */
    public String getPassword() {
        
        return this.password;
    }

    /**
     * Gets the user's salt.
     * 
     * @return The salt of the user.
     */
    public String getSalt() {
        
        return this.salt;
    }

    /**
     * Gets the user's rating.
     * 
     * @return The rating of the user.
     */
    public int getRating() {
        
        return this.rating;
    }

    /** 
     * Sets the user's password.
     * 
     * @param password The new password of the user.
     */
    public void setPassword(String password) {
        
        this.password = password;
    }

    /**
     * Sets the user's salt.
     * 
     * @param salt The new salt of the user.
     */
    public void setSalt(String salt) {
        
        this.salt = salt;
    }

    /**
     * Sets the user's rating.
     * 
     * @param rating The new rating of the user.
     */
    public void setRating(int rating) {
        
        this.rating = rating;
    }

    /**
     * Gets the user's token.
     * 
     * @return The token of the user.
     */
    public String getToken() {
        
        return this.token;
    }

    /**
     * Sets the user's token.
     * 
     * @param token The new token of the user.
     */
    public void setToken(String token) {
        
        this.token = token;
    }


    @Override
    public int compareTo(User other) {
        
        return this.username.compareTo(other.username);
    }
}
