package pt.up.fe.cpd2324.server;

import java.io.IOException;

import pt.up.fe.cpd2324.common.Connection;
import pt.up.fe.cpd2324.common.Message;
import pt.up.fe.cpd2324.common.TreeSet;
import pt.up.fe.cpd2324.client.Player;
import pt.up.fe.cpd2324.queue.NormalQueue;
import pt.up.fe.cpd2324.queue.RankedQueue;

// Manages the queues for normal and ranked games
public class QueueManager implements Runnable {
    private final TreeSet<Player> players;
    private final TreeSet<Player> pendingPlayers = new TreeSet<>(); // Ad-hoc solution to avoid asking the same player multiple times
    
    private final NormalQueue<Player> normalQueue;
    private final RankedQueue<Player> rankedQueue;

    public QueueManager(TreeSet<Player> players, NormalQueue<Player> normalQueue, RankedQueue<Player> rankedQueue) {
        this.players = players;
        this.normalQueue = normalQueue;
        this.rankedQueue = rankedQueue;
    }
  
    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(1000);

                // For each available player, ask if they want to play normal or ranked
                for (Player player : this.players) {
                    if (this.normalQueue.contains(player) || this.rankedQueue.contains(player) || this.pendingPlayers.contains(player)) {
                        continue;
                    }
                    
                    if (player.isPlaying()) {
                        continue;
                    }
                    
                    // Start a new thread to ask the player for the game mode
                    Thread.ofVirtual().start(() -> {
                        try {
                            this.askGameMode(player);
                        } catch (IOException | NullPointerException e) {
                            System.out.println("Error asking game mode: " + e.getMessage());
                        }
                    });
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Error running the queue manager: " + e.getMessage());
        }
    }

    private void askGameMode(Player player) throws IOException {
        while (true) {
             // Notify the player that they can choose a game mode
             Connection.send(player.getSocket(), new Message(Message.Type.MODE, null));

            String[] menu = {
                " ______________________",
                "|                      |",
                "|  Game Mode           |",
                "|                      |",
                "|  1. Normal           |",
                "|  2. Ranked           |",
                "|                      |",
                "|______________________|",
            };

            Connection.show(player.getSocket(), "Your rating: " + player.getRating());

            Connection.show(player.getSocket(), menu);

            // Player is now pending
            this.pendingPlayers.add(player);

            Connection.prompt(player.getSocket(), "Option: ");
            String option = Connection.receive(player.getSocket()).getContent();
           
            if (option.equals("1")) {
                this.normalQueue.add(player);
            } else if (option.equals("2")) {
                this.rankedQueue.add(player);
            } else {
                Connection.error(player.getSocket(), "Invalid option!");
                continue;
            }
 
            this.pendingPlayers.remove(player);

            String gameMode = option.equals("1") ? "normal" : "ranked";
            Connection.ok(player.getSocket(), "Added to the " + gameMode + " queue");
            Connection.show(player.getSocket(), "Waiting for another player to join...");

            System.out.println("Player " + player.getUsername() + " added to the " + gameMode + " queue");

            break;
        }   
    }
}
