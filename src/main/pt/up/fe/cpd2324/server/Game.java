package pt.up.fe.cpd2324.server;

import pt.up.fe.cpd2324.common.Connection;
import pt.up.fe.cpd2324.client.Player;
import pt.up.fe.cpd2324.game.Stones;

import java.io.IOException;

public class Game implements Runnable {
    private final Player player1;
    private final Player player2;

    private final Boolean ranked;
    
    private final Stones stones = new Stones(2);

    private Player currentPlayer;
    private Player otherPlayer;

    public Game(Player player1, Player player2, Boolean ranked) {
        this.player1 = player1;
        this.player2 = player2;
        this.ranked = ranked;

        this.currentPlayer = this.player1;
        this.otherPlayer = this.player2;
    }

    @Override
    public void run() {
        try {
            Connection.send(this.player1.getSocket(), "Game started between you and " + this.player2.getUsername() + "[" + this.player2.getRating() + "]");
            Connection.send(this.player2.getSocket(), "Game started between you and " + this.player1.getUsername() + "[" + this.player1.getRating() + "]");

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
            if (this.ranked) {
                this.updateRatings();
            }

            Connection.send(this.currentPlayer.getSocket(), "You won!");
            Connection.send(this.otherPlayer.getSocket(), "You lost!");

            return;
        }

        // Switch players
        this.switchPlayers();
    }

    private double expectedScore(int rating1, int rating2) {
        return 1 / (1 + Math.pow(10, (rating2 - rating1) / 400.0));
    }

    private void updateRatings() {
        int rating1 = this.currentPlayer.getRating();   // Winner
        int rating2 = this.otherPlayer.getRating();     // Loser

        double expected1 = this.expectedScore(rating1, rating2);
        double expected2 = this.expectedScore(rating2, rating1);

        // K-factor for Elo rating system
        int k = 32;
        
        int newRating1 = (int) (rating1 + k * (1 - expected1));
        int newRating2 = (int) (rating2 + k * (0 - expected2));

        this.currentPlayer.setRating(newRating1);
        this.otherPlayer.setRating(newRating2);

        // Save the new ratings to the database
        Database.getInstance().save();
    }
}
