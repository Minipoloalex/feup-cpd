package pt.up;

import java.io.*;
// import bcrypt



public class Auth {
    private static final String registrationFile = "registration_data.json";
    // private final File file;
    public static void main(String[] args) {
        System.out.println("Testing auth");
    }
    public Auth() {
       // this.file = new File(registrationFile);
    }

    public static boolean authenticate(String username, String password) {
        return true;
    }

    public static boolean usernameExists(String username) {
        return true;
    }

    /**
     *
     * @param username
     * @param password
     * @return true if the user was successfully registered (username did not exist), false otherwise (username already exists)
     */
    public static boolean register(String username, String password) {
        if (usernameExists(username)) {
            return false;
        }
        System.out.println("Registering user " + username);
        return true;
    }
}
