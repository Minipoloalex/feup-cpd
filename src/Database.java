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
    private final String path = "database/db.csv";
    private final Set<Player> players = new TreeSet<>();
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
        File file = new File(this.path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Load the players from the file
        this.load();
    }

    /**
     * Loads the players from the csv file.
     */
    private void load() {
        try {
            Scanner scanner = new Scanner(new File(this.path));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(";");
                this.players.add(new Player(parts[0], parts[1], parts[2], Integer.parseInt(parts[3])));
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the players to the csv file.
     */
    public void save() {
        try {
            FileWriter writer = new FileWriter(this.path);
            for (Player player : this.players) {
                String separator = ";";
                writer.write(player.getUsername() + separator + player.getPassword() + separator + player.getSalt() + separator + player.getRating() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a player to the database.
     * 
     * @param username The username of the player.
     * @param password The password of the player.
     */
    public boolean addPlayer(String username, String password) {
        String salt = generateSalt();
        String hashedPassword = hashPassword(password, salt);
        
        boolean exists = this.playerExists(username);

        if (!exists) {
            Player player = new Player(username, hashedPassword, salt);
            this.players.add(player);
      
            this.save();
        }

        return !exists;
    }

    /**
     * Gets a player from the database.
     * 
     * @param username The username of the player.
     * @return The player with the given username.
     */
    public Player getPlayer(String username) {
        for (Player player : this.players) {
            if (player.getUsername().equals(username)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Checks if a player exists in the database.
     * 
     * @param username The username of the player.
     * @return True if the player exists, false otherwise.
     */
    public boolean playerExists(String username) {
        return this.getPlayer(username) != null;
    }

    /**
     * Checks if the password is correct for a given player.
     * 
     * @param username The username of the player.
     * @param password The password to check.
     * @return True if the password is correct, false otherwise.
     */
    public boolean checkPassword(String username, String password) {
        if (!this.playerExists(username)) {
            return false;
        }

        Player player = this.getPlayer(username);
        String hashedPassword = hashPassword(password, player.getSalt());
        
        return player.getPassword().equals(hashedPassword);
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
     * Update the rating of a player.
     * 
     * @param username The username of the player.
     * @param rating The new rating of the player.
     */
    public void updateRating(String username, int rating) {
        Player player = this.getPlayer(username);
        player.setRating(rating);
        this.save();
    }
}
