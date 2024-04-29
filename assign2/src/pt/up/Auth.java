package pt.up;

import java.io.IOException;


public class Auth {
    private static final Database db;

    static {
        try {
            db = new Database();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Auth() {
        //this.db = db;
    }
    public static boolean login(String username, String password) {
        return db.checkUserPassword(username, password);
    }

    public static boolean existsUsername(String username) {
        return db.existsUsername(username);
    }

    /**
     *
     * @param username username of the user to register
     * @param password plaintext password of the user to store
     * @return true if the user was successfully registered (username did not
     *         exist), false otherwise (username already exists)
     */
    public static boolean register(String username, String password) {
        if (existsUsername(username)) {
            return false;
        }
        return db.storeNewUser(username, password);
    }
    public static void saveUsers() {
        try {
            db.saveUsers();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {

    }
}
