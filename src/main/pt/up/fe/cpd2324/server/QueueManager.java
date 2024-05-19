package pt.up.fe.cpd2324.server;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import pt.up.fe.cpd2324.common.Connection;
import pt.up.fe.cpd2324.common.Message;
import pt.up.fe.cpd2324.client.Player;
import pt.up.fe.cpd2324.queue.NormalQueue;
import pt.up.fe.cpd2324.queue.RankedQueue;

// Manages the queues for normal and ranked games
public class QueueManager implements Runnable {
    private final Set<Player> availablePlayers;
    private final Set<Player> pendingPlayers = new TreeSet<>(); // Ad-hoc solution to avoid asking the same player multiple times
    
    private final NormalQueue<Player> normalQueue;
    private final RankedQueue<Player> rankedQueue;

    public QueueManager(Set<Player> players, NormalQueue<Player> normalQueue, RankedQueue<Player> rankedQueue) {
        this.availablePlayers = players;
        this.normalQueue = normalQueue;
        this.rankedQueue = rankedQueue;
    }
  
    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(1000);

                // For each available player, ask if they want to play normal or ranked
                for (Player player : this.availablePlayers) {
                    if (this.pendingPlayers.contains(player)) {
                        continue;
                    }
                    
                    // Notify the player before asking for the desired game mode
                    Connection.send(player.getSocket(), new Message(Message.Type.MODE, null));
                    
                    // Start a new thread to handle the player's response
                    Thread.ofVirtual().start(() -> {
                        try {
                            this.handlePlayer(player);
                        } catch (IOException e) {
                            System.out.println("Error adding player to queue: " + e.getMessage());
                        }
                    });
                }
            }
        } catch (InterruptedException | IOException e) {
            System.out.println("Error running the queue manager: " + e.getMessage());
        }
    }

    private void handlePlayer(Player player) throws IOException {
        while (true) {
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

            Connection.show(player.getSocket(), menu);

            // Player is now pending
            this.pendingPlayers.add(player);

            Connection.prompt(player.getSocket(), "Option: ");
            String option = Connection.receive(player.getSocket()).getContent();

            if (!option.equals("1") && !option.equals("2")) {
                Connection.error(player.getSocket(), "Invalid option!");
                continue;
            }

            if (option.equals("1")) {
                this.normalQueue.add(player);
            } else {
                this.rankedQueue.add(player);
            }
 
            this.availablePlayers.remove(player);
            this.pendingPlayers.remove(player);

            String gameMode = option.equals("1") ? "Normal" : "Ranked";
            Connection.ok(player.getSocket(), "Added to the " + gameMode + " queue");
            Connection.show(player.getSocket(), "Waiting for another player to join...");

            System.out.println("Player " + player.getUsername() + " added to the " + gameMode + " queue");

            break;
        }   
    }
}