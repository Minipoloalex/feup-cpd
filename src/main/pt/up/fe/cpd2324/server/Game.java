package pt.up.fe.cpd2324.server;

import pt.up.fe.cpd2324.common.Connection;
import pt.up.fe.cpd2324.common.Message;
import pt.up.fe.cpd2324.client.Player;
import pt.up.fe.cpd2324.game.Stones;

import java.io.IOException;

// Represents a game between two players
// Handles the game logic and interactions with the players
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
            System.out.println("Starting game between " + this.player1.getUsername() + " and " + this.player2.getUsername());
            
            // Notify the players that the game is starting
            Connection.send(this.player1.getSocket(), new Message(Message.Type.GAME, null));
            Connection.send(this.player2.getSocket(), new Message(Message.Type.GAME, null));

            while (!this.stones.isGameOver()) {
                this.takeTurn();
            }

            // Notify the players of the game result
            Connection.send(this.currentPlayer.getSocket(), new Message(Message.Type.GAME_OVER, "You won!"));
            Connection.send(this.otherPlayer.getSocket(), new Message(Message.Type.GAME_OVER, "You lost!"));

            // Update the ratings and show the new ratings to the players if the game is ranked
            if (this.ranked) {
                this.updateRatings();
                this.showNewRating();
            }

            System.out.println("Game between " + this.player1.getUsername() + " and " + this.player2.getUsername() + " ended");
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
        // Show the current state of the game to both players
        String[] state = this.stones.toString().split("\n");
        
        Connection.clear(this.currentPlayer.getSocket());
        Connection.clear(this.otherPlayer.getSocket());
        
        if (ranked) {
            this.showRating();
        }

        Connection.show(this.currentPlayer.getSocket(), state);
        Connection.show(this.otherPlayer.getSocket(), state);
        
        Connection.send(this.otherPlayer.getSocket(), new Message(Message.Type.WAIT, "Waiting for the other player...")); 
        
        // Prompt the current player for their move
        Connection.prompt(this.currentPlayer.getSocket(), "Enter your move (stack numStones): ");

        // Receive and process the move
        String move = Connection.receive(this.currentPlayer.getSocket()).getContent();
        String[] parts = move.split(" ");
        int stack = Integer.parseInt(parts[0]);
        int numStones = Integer.parseInt(parts[1]);

        this.stones.removeStones(stack - 1, numStones);


        if (this.stones.isGameOver()) {
            return;
        }

        this.switchPlayers();
    }

    // Calculate the expected score of a player in a game
    private double expectedScore(int rating1, int rating2) {
        return 1 / (1 + Math.pow(10, (rating2 - rating1) / 400.0));
    }
    
    // Update the ratings of the players based on the outcome of the game
    // Winner is always the current player
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

    private void showRating() throws IOException {
        Connection.show(this.currentPlayer.getSocket(), new String[] {
            "Your rating: " + this.currentPlayer.getRating(),
            this.otherPlayer.getUsername() + "'s rating: " + this.otherPlayer.getRating(),
            "\n"
        });

        Connection.show(this.otherPlayer.getSocket(), new String[] {
            "Your rating: " + this.otherPlayer.getRating(),
            this.currentPlayer.getUsername() + "'s rating: " + this.currentPlayer.getRating(),
            "\n"
        });
    }

    private void showNewRating() throws IOException {
        Connection.show(this.currentPlayer.getSocket(), "Your new rating: " + this.currentPlayer.getRating());
        Connection.show(this.otherPlayer.getSocket(), "Your new rating: " + this.otherPlayer.getRating());
    }
}
