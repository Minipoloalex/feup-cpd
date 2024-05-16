package pt.up;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import pt.up.Queues.*;

public class GameManager implements Runnable {
    public static final int GAMEPLAYERS = 2;

    private final ExecutorService gameNormalPool;
    private final Queue normalQueue;
    // private final ExecutorService gameRankedPool;
    // private final Queue rankedQueue;

    public GameManager() {
        this.normalQueue = new NormalQueue();
        this.gameNormalPool = Executors.newCachedThreadPool();
        // this.gameRankedPool = Executors.newCachedThreadPool();
    }

    public boolean addNormalUser(User user) {
        return this.normalQueue.add(user);
    }

    public LinkedList<User> getGamePlayers() {
        LinkedList<User> players = new LinkedList<>();

        for (int i = 0; i < GAMEPLAYERS; i++) {
            players.add(this.normalQueue.pop());
        }

        return players;
    }

    public void manage() {
        if (this.normalQueue.getPlayers() >= GAMEPLAYERS) {
            // start game
            var players = this.getGamePlayers();
            this.gameNormalPool.execute(new Game(players));
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                synchronized (this) {
                    this.wait(100); // Save CPU time
                }
                this.manage();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
