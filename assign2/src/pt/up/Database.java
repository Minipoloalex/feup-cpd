package pt.up;

import java.io.*;
import java.security.SecureRandom;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Database {
    private final static String USERS_FILE = "src/pt/up/storage/users.csv";
    private final static int SALT_SIZE = 16;
    private static final String HASH_ALGORITHM = "SHA-512";

    /**
     * Mapping of username to user
     */
    private final Map<String, User> users = new HashMap<>();
    /**
     * Users sorted by their rating to help in matchmaking.
     */
    private final Set<User> usersSortedByRating = new TreeSet<>();

    public Database() throws IOException {
        loadUsers();
    }
    public synchronized List<User> getUsersSortedByRating() {
        return new ArrayList<>(usersSortedByRating);
    }

    private void loadUsers() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userInfo = line.split(";");
                String username = userInfo[0];
                String hashedPassword = userInfo[1];
                String salt = userInfo[2];
                int rating = Integer.parseInt(userInfo[3]);
                User user = new User(username, hashedPassword, salt, rating);
                users.put(username, user);
                usersSortedByRating.add(user);
            }
        }
    }
    public synchronized void saveUsers() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (User user : users.values()) {
                writer.write(user.toCSV());
                writer.newLine();
            }
        }
    }

    private static String generateSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[SALT_SIZE];
        secureRandom.nextBytes(salt);
        return Arrays.toString(salt);
    }

    private static String hashPassword(String password, String salt) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
        messageDigest.update(salt.getBytes());
        byte[] hashedPassword = messageDigest.digest(password.getBytes());
        return Arrays.toString(hashedPassword);
    }

    private synchronized User getUser(String username) {
        return users.get(username);
    }
    public boolean checkUserPassword(String username, String password) {
        String hashedPassword;
        String salt;
        User u;
        synchronized (this) {
            u = users.get(username);
        }
        if (u == null) {
            return false;
        }
        hashedPassword = u.getHashedPassword();
        salt = u.getSalt();
        try {
            return hashedPassword.equals(hashPassword(password, salt));
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error checking user password " + e.getMessage());
            return false;
        }
    }
    public boolean storeNewUser(String username, String password) {
        try {
            String salt = generateSalt();
            String hashedPassword = hashPassword(password, salt);
            User user = new User(username, hashedPassword, salt);
            synchronized (this) {
                if (existsUsername(username)) {
                    return false;
                }
                usersSortedByRating.add(user);  // nobody else can access the Database so the user is not accessible yet
                users.put(username, user);
                return true;
            }
        } catch (NoSuchAlgorithmException e) {    //  | IOException e
            System.err.println("Error storing new user " + e.getMessage());
            return false;
        }
    }
    public synchronized boolean existsUsername(String username) {
        return users.containsKey(username);
    }

    public synchronized boolean updateRating(String username, int newRating) {
        User user = getUser(username);
        if (user == null) {
            return false;
        }
        usersSortedByRating.remove(user);
        user.setRating(newRating);
        usersSortedByRating.add(user);
        return true;
    }

    public static void main(String[] args) throws IOException {
        Database db = new Database();
        boolean stored = db.storeNewUser("user1", "password");
        assert stored : "Failed to store user1, maybe you already ran this script saving it?";

        stored = db.storeNewUser("user2", "password");
        assert stored : "Failed to store user2";

        boolean exists = db.existsUsername("user1");
        assert exists : "User 1 does not exist";

        exists = db.existsUsername("user3");
        assert !exists : "User 3 exists and should not";

        boolean checked = db.checkUserPassword("user1", "password");
        assert checked : "Password is incorrect when it should be correct";

        checked = db.checkUserPassword("user1", "wrongpassword");
        assert !checked : "Incorrect password but returned correct";

        boolean updated = db.updateRating("user1", 1200);
        assert updated : "Did not update rating";

        updated = db.updateRating("user3", 100);
        assert !updated : "Updated rating of non-existent user";

        List<User> users = db.getUsersSortedByRating();
        assert users.size() == 2 : "There should be 2 users in the database";
        assert users.getFirst().getUsername().equals("user2") : "The first user sorted ascending by rating should be user2";

        db.saveUsers();
    }
}
