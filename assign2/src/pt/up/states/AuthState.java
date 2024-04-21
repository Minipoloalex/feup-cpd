package pt.up.states;
import java.io.PrintWriter;

import pt.up.Auth;

public class AuthState extends State {
    protected String username = null;
    public AuthState(PrintWriter out) {
        super(out);
    }

    public State handle(String inputLine) {
        System.out.println("Handling input: " + inputLine);
        if (username == null) {
            if (Auth.usernameExists(inputLine)) {
                username = inputLine;
            } else {
                out.println("Username does not exist");
            }
        }
        else if (Auth.authenticate(username, inputLine)) {
            out.println("Authentication successful");
            return new MenuState(out);
        } else {
            out.println("Authentication failed");
        }
        return null;
    }
    public void render() {
        System.out.println("Rendering AuthState");
        if (this.username == null) {
            System.out.println("username");
            out.println("Introduce your username");
        }
        else {
            System.out.println("password");
            out.println("Introduce your password");
        }
    }
    public void onEnter() {
        System.out.println("Entering AuthState");
        out.println("Authentication required");
    }

    @Override
    public void onExit() {
        System.out.println("Exiting AuthState");
    }
}
