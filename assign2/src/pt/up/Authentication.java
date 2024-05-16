package pt.up;

/**
 * Class that represents the authentication system.
 */
public class Authentication {
    
    private final Database database;

    /**
     * Constructor for the Authentication class.
     */
    public Authentication() {
        
        this.database = new Database("storage/db.csv");
    }

    /**
     * Logs in a user.
     * 
     * @param username The username of the user.
     * @param password The password of the user.
     * @return True if the login was successful, false otherwise.
     */
    public boolean login(String username, String password) {
        
        return this.database.checkPassword(username, password);
    }
    
    /**
     * Registers a user.
     * 
     * @param username The username of the user.
     * @param password The password of the user.
     * @return True if the registration was successful, false otherwise.
     */
    public boolean register(String username, String password) {
        
        // Check if the user already exists
        if (this.database.userExists(username)) {
            return false;
        }

        // Add the user to the databases
        User user = new User(username, password);
        this.database.addUser(user);
        this.database.save();
        
        return true;
    }

    /**
     * Checks if a username exists.
     * 
     * @param username The username to check.
     * @return True if the username exists, false otherwise.
     */
    public boolean usernameExists(String username) {
        
        return this.database.userExists(username);
    }

    /**
     * Gets the user with the given username.
     * 
     * @param username The username of the user.
     * @return The user with the given username.
     */
    public User getUser(String username) {
        
        return this.database.getUser(username);
    }
}   
