package pt.up.queues;

import java.util.LinkedList;
import java.util.List;

public class NormalQueue<T> extends Queue<T> {
    private final List<T> _queue;

    public NormalQueue() {
        this._queue = new LinkedList<>();
    }

    @Override
    public boolean add(T x) {
        if (this.contains(x)) {
            return false; // T already in queue
        }
        return this._queue.add(x);
    }

    @Override
    public boolean remove(T x) {
        return this._queue.remove(x);
    }

    @Override
    public T pop() {
        return this._queue.remove(0);
    }

    @Override
    public boolean contains(T x) {
        return this._queue.contains(x);
    }

    @Override
    public int getPlayers() {
        return this._queue.size();
    }

    @Override
    public boolean canStartGame(int players) {
        return this.getPlayers() >= players;
    }
}
