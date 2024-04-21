package pt.up.states;

import pt.up.Auth;

import java.io.PrintWriter;

public class LoginState extends State {
    private String username = null;

    public LoginState(PrintWriter out) {
        super(out);
    }
    private void handleUsername(String inputLine) {
        if (Auth.usernameExists(inputLine)) {
            username = inputLine;
        } else {
            out.println("Username does not exist");
        }
    }
    @Override
    public State handle(String inputLine) {
        System.out.println("Handling input: " + inputLine);
        if (username == null) {
            if (inputLine.isEmpty()) {
                return new AuthMenuState(out);
            }
            this.handleUsername(inputLine);
        }
        else {
            if (inputLine.isEmpty()) {
                username = null;
            }
            else if (Auth.authenticate(username, inputLine)) {
                out.println("Authentication successful");
                return new PlayMenuState(out);
            }
            else {
                username = null;
                out.println("Authentication failed");
            }
        }
        return null;
    }
    @Override
    public void render() {
        System.out.println("Rendering AuthMenuState");
        if (this.username == null) {
            System.out.println("username");
            out.println("Introduce your username");
        }
        else {
            System.out.println("password");
            out.println("Introduce your password");
        }
    }
    @Override
    public void onEnter() {
        System.out.println("Entering AuthMenuState");
        out.println("Login to play the game");
        out.println("Type an empty line to return to the Login/Register menu");
    }

    @Override
    public void onExit() {
        System.out.println("Exiting AuthMenuState");
    }
}
