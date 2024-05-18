import java.io.IOException;

public class Game implements Runnable {
    private final Player player1;
    private final Player player2;
    private Player currentPlayer;
    private Player otherPlayer;
    
    private final Stones stones = new Stones(2);

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;

        this.currentPlayer = this.player1;
        this.otherPlayer = this.player2;
    }

    @Override
    public void run() {
        try {
            Connection.send(this.player1.getSocket(), "Game started between you and " + this.player2.getUsername());
            Connection.send(this.player2.getSocket(), "Game started between you and " + this.player1.getUsername());

            System.out.println("Game started between " + this.player1.getUsername() + " and " + this.player2.getUsername());

            while (!this.stones.isGameOver()) {
                this.takeTurn();
            }

            System.out.println("Game over between " + this.player1.getUsername() + " and " + this.player2.getUsername());
        } catch (IOException e) {
            System.out.println("Error running the game: " + e.getMessage());
        }
    }

    private void switchPlayers() {
        Player temp = this.currentPlayer;
        this.currentPlayer = this.otherPlayer;
        this.otherPlayer = temp;
    }

    public void takeTurn() throws IOException {
        String[] lines = this.stones.toString().split("\n");

        for (String line : lines) {
            Connection.send(this.currentPlayer.getSocket(), line);
            Connection.send(this.otherPlayer.getSocket(), line);
        }

        Connection.send(this.otherPlayer.getSocket(), "\n");
        Connection.send(this.otherPlayer.getSocket(), "Waiting for the other player to make a move...");

        Connection.send(this.currentPlayer.getSocket(), "\n");
        Connection.send(this.currentPlayer.getSocket(), "Enter the stack number and the number of stones to remove (e.g. 1 3): ");

        // Receive and process the move
        String move = Connection.receive(this.currentPlayer.getSocket());
        String[] parts = move.split(" ");
        int stack = Integer.parseInt(parts[0]);
        int numStones = Integer.parseInt(parts[1]);

        this.stones.removeStones(stack - 1, numStones);

        // Check if the game is over
        if (this.stones.isGameOver()) {
            Connection.send(this.currentPlayer.getSocket(), "You won!");
            Connection.send(this.otherPlayer.getSocket(), "You lost!");

            return;
        }

        // Switch players
        this.switchPlayers();
    }
}
