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

    private long lastPing = System.currentTimeMillis();

    public QueueManager(TreeSet<Player> players, NormalQueue<Player> normalQueue, RankedQueue<Player> rankedQueue) {
        this.players = players;
        this.normalQueue = normalQueue;
        this.rankedQueue = rankedQueue;
    }
  
    @Override
    public void run() {
        try {

            Thread.ofVirtual().start(() -> {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        this.pingPlayers();
                    } catch (InterruptedException e) {
                        System.out.println("Error sleeping thread: " + e.getMessage());
                    }
                }
            });
            
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

            Connection.show(player.getSocket(), "Your rating: " + player.getRating());

            Connection.show(player.getSocket(), menu);

            // Player is now pending
            this.pendingPlayers.add(player);

            Connection.prompt(player.getSocket(), "Option: ");

            String option = null;
            try {
                option = Connection.receive(player.getSocket()).getContent();
            } catch (IOException e) {
                System.out.println("Error receiving option: " + e.getMessage());
            }

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

    private void pingPlayers() {
        if (System.currentTimeMillis() - this.lastPing < 10000) {   // Ping every 10 seconds
            return;
        }

        lastPing = System.currentTimeMillis();
    
        for (Player player : this.normalQueue) {
            try {
                Connection.ping(player.getSocket());
            } catch (IOException e) {
                System.out.println("Error pinging player: " + e.getMessage());
                this.normalQueue.remove(player);
                System.out.println("Player " + player.getUsername() + " removed from the normal queue");
            }
        }

        for (Player player : this.rankedQueue) {
            try {
                Connection.ping(player.getSocket());
            } catch (IOException e) {
                System.out.println("Error pinging player: " + e.getMessage());
                this.rankedQueue.remove(player);
                System.out.println("Player " + player.getUsername() + " removed from the ranked queue");
            }
        }
    }
}
