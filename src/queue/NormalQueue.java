package queue;

import java.util.LinkedList;
import java.util.List;

public class NormalQueue<T> extends Queue<T> {
    private final List<T> queue;

    public NormalQueue() {
        super();
        this.queue = new LinkedList<>();
    }

    @Override
    public boolean add(T x) {
        this.lock.lock();
        try {
            if (this.contains(x)) {
                return false;
            }
            return this.queue.add(x);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public boolean remove(T x) {
        this.lock.lock();
        try {
            return this.queue.remove(x);
        } finally {
            this.lock.unlock();
        }
    }

    public T pop() {
        this.lock.lock();
        try {
            return this.queue.remove(0);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public boolean contains(T x) {
        this.lock.lock();
        try {
            return this.queue.contains(x);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public int getSize() {
        this.lock.lock();
        try {
            return this.queue.size();
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public boolean canStartGame() {
        this.lock.lock();
        try {
            return this._queue.size() >= 2;
        } finally {
            this.lock.unlock();
        }
    }
}
