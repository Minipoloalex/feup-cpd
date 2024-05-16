package pt.up;

import java.net.Socket;
import java.util.List;

public class Game implements Runnable {
    private List<Socket> userSockets;
    private List<Player> players;

    public Game(List<Player> players) {
        this.players = players;
    }

    public Game(int players, List<Socket> userSockets) {
        this.userSockets = userSockets;
    }

    public void start() {
        System.out.println("Starting game with " + userSockets.size() + " players");
    }

    @Override
    public void run() {
        System.out.println("Game started");
    }
}
