package pt.up.states;

import pt.up.Connection;

public final class AuthMenuState extends State {
    public AuthMenuState(Connection connection) {
        super(connection);
        onEnter();
    }

    @Override
    public State handle(String inputLine) {
        if (inputLine.equals("0")) {
            return null; // Exit game
        }
        if (inputLine.equals("1")) {
            return new LoginState(this.connection);
        }
        if (inputLine.equals("2")) {
            System.out.println("Handling input: " + inputLine);
            return new RegisterState(this.connection);
        }
        out.println("Invalid option");
        return this;
    }

    @Override
    public void render() {
        out.println("Select an option");
        out.println("0. Exit");
        out.println("1. Login");
        out.println("2. Register");
    }

    @Override
    public void onEnter() {
        out.println("Welcome!");
        out.println("Authentication is required to play the game");
    }

    @Override
    public void onExit() {
    }
}
