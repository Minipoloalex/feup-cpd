import java.io.IOException;

public class Game implements Runnable {
    private final Player player1;
    private final Player player2;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    @Override
    public void run() {
        try {
            String message = "Game started between " + this.player1.getUsername() + " and " + this.player2.getUsername();
            Connection.send(this.player1.getSocket(), message);
            Connection.send(this.player2.getSocket(), message);

            System.out.println("Game started between " + this.player1.getUsername() + " and " + this.player2.getUsername());
        } catch (IOException e) {
            System.out.println("Error running the game: " + e.getMessage());
        }
    }
}
