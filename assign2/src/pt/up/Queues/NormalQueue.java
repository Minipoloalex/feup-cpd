package pt.up.Queues;

import pt.up.User;

import java.util.LinkedList;
import java.util.List;

public class NormalQueue extends Queue {
    private final List<User> _queue;

    public NormalQueue() {
        this._queue = new LinkedList<>();
    }

    @Override
    public boolean add(User user) {
        if (this.contains(user)) {
            return false; // User already in queue
        }
        return this._queue.add(user);
    }

    @Override
    public boolean remove(User user) {
        return this._queue.remove(user);
    }

    @Override
    public User pop() {
        return this._queue.remove(0);
    }

    @Override
    public boolean contains(User user) {
        return this._queue.contains(user);
    }

    @Override
    public int getPlayers() {
        return this._queue.size();
    }
}
