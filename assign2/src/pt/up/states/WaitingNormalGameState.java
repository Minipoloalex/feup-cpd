package pt.up.states;

import pt.up.Connection;
import pt.up.Message;

public final class WaitingNormalGameState extends State {
    private Thread waitForGame;
    private boolean accept = false;

    public WaitingNormalGameState(Connection connection, String username, String token) {
        super(connection, username, token);
        onEnter();
    }

    @Override
    public State handle(String inputLine) {
        if (inputLine.equals("back") || (accept && inputLine.equals("n"))) {
            // send message to server to cancel the game
            return new PlayMenuState(connection, username, token);
        }
        if (inputLine.equals("y") && accept) {
            // start game
        }
        return this;
    }

    @Override
    public void render() {
        out.println("Type 'back' to return to the Play menu");
    }

    @Override
    public void onEnter() {
        out.println("\n\n\nWaiting for a game to start");
        waitForGame = Thread.ofVirtual().start(() -> {
            Message response = connection.playNormalGame(username, token);
            System.out.println(response.getContent());
            accept = true;
        });
    }

    @Override
    public void onExit() {
        waitForGame.interrupt();
    }
}
