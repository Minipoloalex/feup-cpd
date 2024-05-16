package pt.up.states;

import pt.up.Connection;
import pt.up.Message;

public final class RegisterState extends State {
    private String username = null;

    public RegisterState(Connection connection) {
        super(connection);
        onEnter();
    }

    @Override
    public State handle(String inputLine) {
        if (username == null) {
            if (inputLine.equals("back")) {
                return new AuthMenuState(this.connection);
            }
            username = inputLine;
        } else {
            Message response = connection.register(username, inputLine);
            if (response.isOk()) {
                out.println("Registration successful");
                return new LoginState(this.connection);
            } else {
                out.println(response);
                username = null;
            }
        }
        return this;
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
        out.println("\n\n\nRegister to play the game");
        out.println("Type 'back' to go back to the Login/Register menu");
    }

    @Override
    public void onExit() {

    }
}
