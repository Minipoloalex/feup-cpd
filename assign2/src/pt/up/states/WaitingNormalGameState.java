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
            connection.sendRequest(Message.error("Game refused"));
            Message response = connection.listen();
            System.out.println(response.getContent());
            return new PlayMenuState(connection, username, token);
        }
        if (inputLine.equals("y") && accept) {
            System.out.println("Game accepted");
            connection.sendRequest(Message.ok());
            Message response = connection.listen();

            if (response.isOk()) {
                System.out.println("Game started");
                System.exit(0);
                // return new NormalGameState(connection, username, token);
            } else {
                System.out.println(response.getContent());
                return new PlayMenuState(connection, username, token);
            }
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
            out.println("Sent play request");
            Message response = connection.playNormalGame(username, token);

            if (response.isError()) {
                out.println(response.getContent());
                return;
            } else {
                out.println("Waiting for game confirmation");
            }

            Message gameConfirmation = connection.listen();
            System.out.println("Received game confirmation");
            System.out.println(gameConfirmation.getContent());
            accept = true;
        });
    }

    @Override
    public void onExit() {
        waitForGame.interrupt();
    }
}
