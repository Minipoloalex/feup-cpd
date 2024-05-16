package pt.up.states;

import pt.up.Connection;

public final class NormalGameState extends State {
    private Thread waitForGame;
    private boolean accept = false;

    public NormalGameState(Connection connection, String username, String token) {
        super(connection, username, token);
        onEnter();
    }

    @Override
    public State handle(String inputLine) {
        return this;
    }

    @Override
    public void render() {
        out.println("Playing the game!");
    }

    @Override
    public void onEnter() {
        out.println("\n\n\nGame started!");
    }

    @Override
    public void onExit() {
        waitForGame.interrupt();
    }
}
