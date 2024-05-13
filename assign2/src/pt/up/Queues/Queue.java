package pt.up.Queues;

import pt.up.User;

import java.util.LinkedList;
import java.util.List;

public abstract class Queue {
    LinkedList<User> queue;

    public Queue() {
        queue = new LinkedList<>();
    }

    public synchronized boolean add(User user) {
        return this.queue.add(user);
    }

    public synchronized boolean remove(User user) {
        return this.queue.remove(user);
    }

    public synchronized boolean contains(User user) {
        return this.queue.contains(user);
    }

    public abstract List<User> startGame(int size);
}
