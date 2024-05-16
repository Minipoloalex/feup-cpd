package pt.up.states;

import java.io.IOException;
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
        if (inputLine.equals("back")) {
            // waitForGame.interrupt();
            connection.sendRequest(Message.leave(username, token));
            connection.listen();
            return new PlayMenuState(connection, username, token);
        }

        if (accept && inputLine.equals("n")) {
            return rejectGame();
        }

        if (inputLine.equals("y") && accept) {
            return acceptGame();
        }

        return this;
    }

    private State acceptGame() {
        connection.sendRequest(Message.ok());
        Message response = connection.listen();

        if (response.isOk()) {
            return new NormalGameState(connection, username, token);
        }
        System.out.println(response.getContent());
        return new PlayMenuState(connection, username, token);
    }

    private State rejectGame() {
        connection.sendRequest(Message.error("Game refused"));
        Message response = connection.listen();
        System.out.println(response.getContent());
        return new PlayMenuState(connection, username, token);
    }

    @Override
    public void render() {
        out.println("Type 'back' to return to the Play menu");
    }

    @Override
    public void onEnter() {
        out.println("\n\n\nWaiting for a game to start");
        waitForGame = Thread.ofVirtual().start(() -> {
            try {
                out.println("Sent play request");
                Connection replica = connection.replicate();
                Message response = replica.playNormalGame(username, token);

                if (response.isError()) {
                    out.println(response.getContent());
                    return;
                } else {
                    out.println("Waiting for game confirmation");
                }

                Message gameConfirmation = replica.listen();
                System.out.println("Received game confirmation");
                System.out.println(gameConfirmation.getContent());
                accept = true;
            } catch (IOException e) {
                System.out.println("Game request was interrupted");
            }
        });
    }

    @Override
    public void onExit() {
        waitForGame.interrupt();
    }
}
