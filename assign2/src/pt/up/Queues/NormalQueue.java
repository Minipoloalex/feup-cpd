package pt.up.Queues;

import pt.up.User;

import java.util.LinkedList;
import java.util.List;

public class NormalQueue extends Queue implements AutoCloseable {
     static NormalQueue singleton;

    private NormalQueue() {
        super();
        singleton = this;
    }

    public static NormalQueue getQueueObject() {
        if (singleton == null) {
            return new NormalQueue();
        }
        return singleton;
    }

    @Override
    public synchronized List<User> startGame(int size) {
        List<User> ret = new LinkedList<>();

        for (int i = 0; i < size; i++) {
            ret.add(this.queue.remove());
        }

        return ret;
    }

    @Override
    public void close() throws Exception {
        this.queue.clear();
    }
}
