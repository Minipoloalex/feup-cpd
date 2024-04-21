package pt.up.states;

import java.io.PrintWriter;

public abstract class State {
    protected final PrintWriter out;

    public State(PrintWriter out) {
        this.out = out;
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
}
