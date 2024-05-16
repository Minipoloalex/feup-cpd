package pt.up;

import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class that represents a database of users.
 * 
 * The database is stored in a csv file with the following format:
 * username;password;rating
 */
public class Database {
    
    private final String path;
    private final String separator = ";";
    private final Set<User> users = new TreeSet<>();
    
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * Constructor for the Database class.
     * 
     * @param path Path to the csv file.
     */
    public Database(String path) {
        
        this.path = path;
        
        // Create the file if it doesn't exist
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Load the users from the file
        this.load();
    }

    /**
     * Loads the users from the csv file.
     */
    private void load() {
        
        try {
            Scanner scanner = new Scanner(new File(this.path));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(this.separator);
                this.users.add(new User(parts[0], parts[1], Integer.parseInt(parts[2])));
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the users to the csv file.
     */
    public void save() {
        lock.lock();

        try {
            FileWriter writer = new FileWriter(this.path);
            for (User user : this.users) {
                writer.write(user.getUsername() + this.separator + user.getPassword() + this.separator + user.getRating() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        lock.unlock();
    }

    /**
     * Adds a user to the database.
     * 
     * @param user The user to add.
     */
    public void addUser(User user) {
        
        this.users.add(user);

        // Save the users to the file
        this.save();
    }

    /**
     * Gets a user from the database.
     * 
     * @param username The username of the user.
     * @return The user with the given username.
     */
    public User getUser(String username) {
        
        for (User user : this.users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Checks if a user exists in the database.
     * 
     * @param username The username of the user.
     * @return True if the user exists, false otherwise.
     */
    public boolean userExists(String username) {
        
        return this.getUser(username) != null;
    }

    /**
     * Checks if the password is correct for a given user.
     * 
     * @param username The username of the user.
     * @param password The password to check.
     * @return True if the password is correct, false otherwise.
     */
    public boolean checkPassword(String username, String password) {
        
        User user = this.getUser(username);
        return user != null && user.getPassword().equals(password);
    }

    /**
     * Generates a token for a user.
     * 
     * @param username The username of the user.
     * @return The generated token.
     */
    public String generateToken(String username) {
        User user = this.getUser(username);
        String token = UUID.randomUUID().toString();
        
        user.setToken(token);
        
        return token;
    }

    /**
     * Checks if a token is valid for a given user.
     * 
     * @param username The username of the user.
     * @param token The token to check.
     * @return True if the token is valid, false otherwise.
     */
    public boolean checkUserToken(String username, String token) {
        return this.getUser(username).getToken().equals(token);
    }
}
