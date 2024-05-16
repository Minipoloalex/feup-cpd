package pt.up.states;

import java.io.PrintWriter;
import pt.up.Connection;

public abstract class State {
    protected final PrintWriter out;
    protected final Connection connection;
    protected String username;
    protected String token;

    public State(Connection connection) {
        this.out = new PrintWriter(System.out, true);
        this.connection = connection;
    }

    public State(Connection connection, String username, String token) {
        this.out = new PrintWriter(System.out, true);
        this.connection = connection;
        this.username = username;
        this.token = token;
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

    public String getToken() {
        return token;
    }
}
