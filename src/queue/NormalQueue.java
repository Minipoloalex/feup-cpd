package queue;

import java.util.LinkedList;
import java.util.List;

public class NormalQueue<T> extends Queue<T> {
    private final List<T> _queue;

    public NormalQueue() {
        this._queue = new LinkedList<>();
    }

    @Override
    public boolean add(T x) {
        this.lock.lock();
        try {
            if (this.contains(x)) {
                return false; 
            }
            return this._queue.add(x);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public boolean remove(T x) {
        this.lock.lock();
        try {
            return this._queue.remove(x);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public T pop() {
        this.lock.lock();
        try {
            return this._queue.remove(0);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public boolean contains(T x) {
        this.lock.lock();
        try {
            return this._queue.contains(x);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public int getSize() {
        this.lock.lock();
        try {
            return this._queue.size();
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public boolean canStartGame(int numPlayers) {
        this.lock.lock();
        try {
            return this._queue.size() >= numPlayers;
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public List<T> getPlayers(int numPlayers) {
        this.lock.lock();
        try {
            List<T> players = new LinkedList<>();
            for (int i = 0; i < numPlayers; i++) {
                players.add(this._queue.remove(0));
            }
            return players;
        } finally {
            this.lock.unlock();
        }
    }
}
