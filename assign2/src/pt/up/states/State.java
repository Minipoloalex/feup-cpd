package pt.up.states;

import java.io.PrintWriter;
import pt.up.User;

public abstract class State {
    protected final PrintWriter out;
    protected User user;

    public State(PrintWriter out) {
        this.out = out;
    }

    public State(PrintWriter out, User u) {
        this.out = out;
        this.user = u;
    }

    /**
     * 
     * @param inputLine the input that came from the client
     * @return the next state if it is different, null if it is the same state
     */
    public abstract State handle(String inputLine);

    public abstract void render();

    public abstract void onEnter();

    public abstract void onExit();

    public User getUser() {
        return this.user;
    }
}
