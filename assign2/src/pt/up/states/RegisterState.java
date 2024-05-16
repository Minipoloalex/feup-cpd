package pt.up.states;

import pt.up.Authentication;

import java.io.PrintWriter;

public class RegisterState extends State {
    
    private static final Authentication auth = new Authentication();
    private String username;

    public RegisterState(PrintWriter out) {
        super(out);
    }

    private void handleUsername(String inputLine) {
        if (auth.usernameExists(inputLine)) {
            out.println("Username already exists");
        } else {
            this.username = inputLine;
        }
    }

    @Override
    public State handle(String inputLine) {
        if (this.username == null) {
            if (inputLine.isEmpty()) {
                return new AuthMenuState(out);
            }
            this.handleUsername(inputLine);
        } else {
            if (auth.register(this.username, inputLine)) {
                out.println("Registration successful");
                return new AuthMenuState(out);
            } else {
                out.println("Registration failed");
            }
            this.username = null;
        }
        return null;
    }

    @Override
    public void render() {
        if (this.username == null) {
            out.println("Introduce the username to register");
        } else {
            out.println("Introduce the password to register");
        }
    }

    @Override
    public void onEnter() {
        out.println("Register to play the game");
        out.println("Type an empty line to go back to the Login/Register menu");
    }

    @Override
    public void onExit() {

    }
}
