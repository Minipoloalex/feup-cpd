package queue;

import java.util.concurrent.locks.ReentrantLock;

public abstract class Queue<T> {
    protected ReentrantLock lock;

    public Queue() {
        this.lock = new ReentrantLock();
    }

    public abstract boolean add(T user);

    public abstract boolean remove(T user);

    public abstract boolean contains(T user);

    public abstract int getSize();  

    public abstract boolean canStartGame();
}
