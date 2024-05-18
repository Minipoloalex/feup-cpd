import java.util.concurrent.ExecutorService;

import queue.*;

public class GameScheduler implements Runnable {
    private final NormalQueue<Player> normalQueue;
    // private final RankedQueue<Player> rankedQueue;

    private final ExecutorService normalPool;
    // private final ExecutorService rankedPool;

    public GameScheduler(NormalQueue<Player> normalQueue, ExecutorService normalPool, ExecutorService rankedPool) {
        this.normalQueue = normalQueue;
        // this.rankedQueue = rankedQueue;
        this.normalPool = normalPool;
        // this.rankedPool = rankedPool;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(1000);

                // Check if there are enough players in the normal queue
                if (this.normalQueue.canStartGame()) {
                    Player player1 = this.normalQueue.pop();
                    Player player2 = this.normalQueue.pop();

                    // Start a new game with the two players
                    Game game = new Game(player1, player2);

                    // Start the game in a new thread
                    this.normalPool.execute(game);
                }

                // Handle the ranked queue

              }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
