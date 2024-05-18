import java.io.IOException;
import java.util.Set;

import queue.*;

public class QueueManager implements Runnable {
    private final Set<Player> players;
    private final NormalQueue<Player> normalQueue;
    // private final RankedQueue<Player> rankedQueue;

    public QueueManager(Set<Player> players, NormalQueue<Player> normalQueue) {
        this.players = players;
        this.normalQueue = normalQueue;
        // this.rankedQueue = rankedQueue;
    }
  
    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(1000);

                // For each available player, ask if they want to play normal or ranked
                for (Player player : this.players) {
                    Connection.send(player.getSocket(), "Do you want to play normal or ranked? (normal/ranked): ");
                    String gameMode = Connection.receive(player.getSocket());
        
                    if (gameMode.equals("normal")) {
                        this.normalQueue.add(player);
                    } else if (gameMode.equals("ranked")) {
                        // this.rankedQueue.add(player);
                    } else {
                        Connection.send(player.getSocket(), "Invalid game mode!");
                        continue;
                    }

                    this.players.remove(player);

                    Connection.send(player.getSocket(), "OK");
                    
                    System.out.println("Player " + player.getUsername() + " added to the " + gameMode + " queue.");
                }
            } 
        } catch (IOException e) {
            System.out.println("Error running the queue manager: " + e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } 
    }
}
