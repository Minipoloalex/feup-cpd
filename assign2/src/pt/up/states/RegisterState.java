package pt.up.states;

import pt.up.Auth;

import java.io.PrintWriter;

public class RegisterState extends State {
    private String username = null;

    public RegisterState(PrintWriter out) {
        super(out, null);
    }

    private void handleUsername(String inputLine) {
        if (Auth.existsUsername(inputLine)) {
            out.println("Username already exists");
        } else {
            username = inputLine;
        }
    }

    @Override
    public State handle(String inputLine) {
        if (username == null) {
            if (inputLine.isEmpty()) {
                return new AuthMenuState(out);
            }
            this.handleUsername(inputLine);
        } else {
            if (Auth.register(username, inputLine)) {
                out.println("Registration successful");
                return new AuthMenuState(out);
            } else {
                out.println("Registration failed");
            }
            username = null;
        }
        return null;
    }

    @Override
    public void render() {
        if (username == null) {
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
