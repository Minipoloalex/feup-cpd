package pt.up.states;
import java.io.PrintWriter;


public class AuthMenuState extends State {
    public AuthMenuState(PrintWriter out) {
        super(out);
    }

    public State handle(String inputLine) {
        if (inputLine.equals("0")) {
            // TODO
            return null;
        }
        else if (inputLine.equals("1")) {
            return new LoginState(out);
        } else if (inputLine.equals("2")) {
            return new RegisterState(out);
        }
        else {
            out.println("Invalid option");
        }
        return null;
    }
    public void render() {
        out.println("Select an option");
        out.println("0. Exit");
        out.println("1. Login");
        out.println("2. Register");
    }
    public void onEnter() {
        out.println("Welcome!");
        out.println("Authentication is required to play the game");
    }

    @Override
    public void onExit() {
    }
}
