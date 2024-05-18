import java.util.List;
import java.util.concurrent.ExecutorService;

import queue.*;

public class GameScheduler implements Runnable {
    private final NormalQueue<Player> normalQueue;
    private final RankedQueue<Player> rankedQueue;

    private final ExecutorService normalPool;
    private final ExecutorService rankedPool;

    private final int NUM_PLAYERS = 2;

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
                    
                    // Start a new game with the two players
                    Game game = new Game(players.get(0), players.get(1));

                    // Start the game in a new thread
                    this.normalPool.execute(game);
                }

                // Check if there are enough players in the ranked queue
              }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
