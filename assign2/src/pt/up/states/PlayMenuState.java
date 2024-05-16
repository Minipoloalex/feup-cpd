package pt.up.states;

import pt.up.Connection;

public final class PlayMenuState extends State {
    public PlayMenuState(Connection connection, String username, String token) {
        super(connection, username, token);
        onEnter();
    }

    @Override
    public State handle(String inputLine) {
        if (inputLine.equals("1")) {
            return new WaitingNormalGameState(connection, username, token);
        } else if (inputLine.equals("2")) {
            // return new RankState(out);
        }
        return this;
    }

    @Override
    public void render() {
        out.println("Select the game mode you want to play");
        out.println("1. Normal game");
        out.println("2. Ranked game");
    }

    @Override
    public void onEnter() {
        out.println("\n\n\nWelcome to the game");
    }

    @Override
    public void onExit() {
    }
}
