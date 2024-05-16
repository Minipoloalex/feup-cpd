package pt.up.queues;

import pt.up.User;

public abstract class Queue {
    public abstract boolean add(User user);

    public abstract boolean remove(User user);

    public abstract User pop();

    public abstract boolean contains(User user);

    public abstract int getPlayers();

    public abstract boolean canStartGame(int players);
}
