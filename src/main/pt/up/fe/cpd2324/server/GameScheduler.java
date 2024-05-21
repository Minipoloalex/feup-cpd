package pt.up.fe.cpd2324.server;

import pt.up.fe.cpd2324.client.Player;
import pt.up.fe.cpd2324.queue.NormalQueue;
import pt.up.fe.cpd2324.queue.RankedQueue;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class GameScheduler implements Runnable {
    private final NormalQueue<Player> normalQueue;
    private final RankedQueue<Player> rankedQueue;

    private final ExecutorService normalPool;
    private final ExecutorService rankedPool;

    private final static int NUM_PLAYERS = 2;

    public GameScheduler(NormalQueue<Player> normalQueue, RankedQueue<Player> rankedQueue, ExecutorService normalPool, ExecutorService rankedPool) {
        this.normalQueue = normalQueue;
        this.rankedQueue = rankedQueue;
        this.normalPool = normalPool;
        this.rankedPool = rankedPool;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(1000);

                // Check if there are enough players in the normal queue
                if (this.normalQueue.canStartGame(NUM_PLAYERS)) {
                    List<Player> players = this.normalQueue.getPlayers(NUM_PLAYERS);
                    
                    Player player1 = players.get(0);
                    Player player2 = players.get(1);

                    player1.setPlaying(true);
                    player2.setPlaying(true);

                    Game game = new Game(player1, player2, false);

                    this.normalPool.execute(game);
                }   
            
                // Match players in the ranked queue if possible
                if (this.rankedQueue.canStartGame(NUM_PLAYERS)) {
                    List<Player> players = this.rankedQueue.getPlayers(NUM_PLAYERS);

                    Player player1 = players.get(0);
                    Player player2 = players.get(1);

                    player1.setPlaying(true);
                    player2.setPlaying(true);
                    
                    Game game = new Game(player1, player2, true);

                    this.rankedPool.execute(game);
                }     
            }
        } catch (InterruptedException e) {
            System.out.println("Error in the game scheduler: " + e.getMessage());
        }
    }
}
