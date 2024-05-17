import java.util.Set;
import java.util.TreeSet;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.util.Scanner;
import java.security.SecureRandom;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Database {
    private final File file;
    private final Set<Client> clients = new TreeSet<>();
    private static final Database instance = new Database();

    /**
     * Gets the instance of the Database class.
     */
    public static Database getInstance() {
        return instance;
    }
    
    /**
     * Constructor for the Database class.
     */
    private Database() {
        // Create the file if it doesn't exist
        this.file = new File("database/db.csv");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Load the clients from the file
        this.load();
    }

    /**
     * Loads the clients from the csv file.
     */
    private void load() {
        try {
            Scanner scanner = new Scanner(this.file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(";");
                this.clients.add(new Client(parts[0], parts[1], parts[2], Integer.parseInt(parts[3])));
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the clients to the csv file.
     */
    public void save() {
        try {
            FileWriter writer = new FileWriter(this.file);
            for (Client client : this.clients) {
                String separator = ";";
                writer.write(client.getUsername() + separator + client.getPassword() + separator + client.getSalt() + separator + client.getRating() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a client to the database.
     * 
     * @param username The username of the client.
     * @param password The password of the client.
     */
    public void addClient(String username, String password) {
        String salt = generateSalt();
        String hashedPassword = hashPassword(password, salt);
        
        this.clients.add(new Client(username, hashedPassword, salt));
        this.save();
    }

    /**
     * Gets a client from the database.
     * 
     * @param username The username of the client.
     * @return The client with the given username.
     */
    public Client getClient(String username) {
        for (Client client : this.clients) {
            if (client.getUsername().equals(username)) {
                return client;
            }
        }
        return null;
    }

    /**
     * Checks if a client exists in the database.
     * 
     * @param username The username of the client.
     * @return True if the client exists, false otherwise.
     */
    public boolean clientExists(String username) {
        return this.getClient(username) != null;
    }

    /**
     * Checks if the password is correct for a given client.
     * 
     * @param username The username of the client.
     * @param password The password to check.
     * @return True if the password is correct, false otherwise.
     */
    public boolean checkPassword(String username, String password) {
        Client client = this.getClient(username);
        String hashedPassword = hashPassword(password, client.getSalt());
        
        return client.getPassword().equals(hashedPassword);
    }

    /**
     * Generate salt for password hashing.
     * 
     * @return The generated salt.
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        return bytesToHex(salt);
    }

    /**
     * Hashes a password with a given salt.
     * 
     * @param password The password to hash.
     * @param salt The salt to use.
     * @return The hashed password.
     */
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(password.getBytes());
            
            return bytesToHex(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Converts bytes to a hexadecimal string.
     * 
     * @param bytes The bytes to convert.
     * @return The hexadecimal string.
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        
        return sb.toString();
    }

    /**
     * Update the rating of a client.
     * 
     * @param username The username of the client.
     * @param rating The new rating of the client.
     */
    public void updateRating(String username, int rating) {
        Client client = this.getClient(username);
        client.setRating(rating);
        this.save();
    }
}
