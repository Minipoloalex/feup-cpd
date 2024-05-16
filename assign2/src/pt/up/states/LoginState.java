package pt.up.states;

import pt.up.Connection;
import pt.up.Message;

public final class LoginState extends State {
    public LoginState(Connection connection) {
        super(connection);
        onEnter();
    }

    @Override
    public State handle(String inputLine) {
        if (inputLine.equals("back")) {
            return new AuthMenuState(connection);
        }
        if (username == null) {
            username = inputLine;
        } else {
            Message response = connection.login(username, inputLine);
            if (response.isOk()) {
                out.println("Authentication successful");
                this.token = response.getContent();
                return new PlayMenuState(connection, username, token);
            }
            username = null;
            out.println("Authentication failed");
        }
        return this;
    }

    @Override
    public void render() {
        if (this.username == null) {
            out.println("Introduce your username:");
        } else {
            out.println("Introduce your password:");
        }
    }

    @Override
    public void onEnter() {
        out.println("\n\n\nLogin to play the game");
        out.println("Type 'back' to return to the Login/Register menu");
    }

    @Override
    public void onExit() {
        System.out.println("Exiting AuthMenuState");
    }
}
