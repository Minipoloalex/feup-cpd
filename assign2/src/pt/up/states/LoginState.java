package pt.up.states;

import java.io.PrintWriter;

import pt.up.Authentication;

public class LoginState extends State {
    
    private static final Authentication auth = new Authentication();
    private String username;

    public LoginState(PrintWriter out) {
        super(out);
    }

    private void handleUsername(String inputLine) {
        if (auth.usernameExists(inputLine)) {
            this.username = inputLine;
        } else {
            out.println("Username does not exist");
        }
    }

    @Override
    public State handle(String inputLine) {
        System.out.println("Handling input: " + inputLine);
        if (this.username == null) {
            if (inputLine.isEmpty()) {
                return new AuthMenuState(out);
            }
            this.handleUsername(inputLine);
        } else {
            if (inputLine.isEmpty()) {
                this.username = null;
            } else if (auth.login(this.username, inputLine)) {
                out.println("Authentication successful");
                return new PlayMenuState(out, auth.getUser(username));
            } else {
                this.username = null;
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
        } else {
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
