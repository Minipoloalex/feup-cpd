package pt.up;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import pt.up.queues.*;

public class GameManager {
    public static final int GAMEPLAYERS = 2;

    private final ExecutorService gameNormalPool;
    private final Queue<Player> normalQueue;
    // private final ExecutorService gameRankedPool;
    // private final Queue rankedQueue;

    public GameManager() {
        this.normalQueue = new NormalQueue<>();
        this.gameNormalPool = Executors.newVirtualThreadPerTaskExecutor();
        // this.gameRankedPool = Executors.newCachedThreadPool();
    }

    public boolean addNormalPlayer(Player user) {
        return this.normalQueue.add(user);
    }

    public LinkedList<Player> getGamePlayers() {
        LinkedList<Player> players = new LinkedList<>();

        for (int i = 0; i < GAMEPLAYERS; i++) {
            players.add(this.normalQueue.pop());
        }

        return players;
    }

    public void manage() {
        if (this.normalQueue.canStartGame(GAMEPLAYERS)) {
            System.out.println("Game can start");
            var players = this.getGamePlayers();
            if (gameConfirmed(players)) {
                // this.gameNormalPool.execute(new Game(players));
                System.out.println("Game started with " + GAMEPLAYERS + " players");
            }
        }
    }

    public boolean gameConfirmed(LinkedList<Player> players) {
        for (Player player : players) {
            // send messages to all first
            player.sendGameConfirmation("Game found! Accept? (y/n)");
            System.out.println("Sent confirmation to " + player.getUsername());
        }

        boolean confirmed = true;
        LinkedList<Boolean> confirmations = new LinkedList<>();

        for (Player player : players) {
            // receive confirmation or denial from all
            boolean confirmation = player.getGameConfirmation();
            System.out.println("Received confirmation from " + player.getUsername() + ": " + confirmation);
            player.sendOk();
            confirmed &= confirmation;
            confirmations.add(confirmation);
        }

        for (int i = 0; i < players.size(); i++) {
            if (!confirmations.get(i)) {
                players.get(i).sendGameCanceled();
            }
        }

        return confirmed;
    }
}
