package queue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RankedQueue<T extends Rateable> extends Queue<T> {
    private class Bucket<TT extends Rateable> {
        private final HashSet<TT> players;
        private final int lower;
        private final int upper;

        public Bucket(int lower, int upper) {
            this.players = new HashSet<>();
            this.lower = lower;
            this.upper = upper;
        }

        public boolean add(TT x) {
            int rating = x.rating();
            if (rating < this.lower || rating > this.upper) {
                return false;
            }
            return this.players.add(x);
        }

        public boolean remove(TT x) {
            return this.players.remove(x);
        }

        public boolean contains(TT x) {
            return this.players.contains(x);
        }

        public int size() {
            return this.players.size();
        }

        public boolean canStartGame(int players) {
            return this.players.size() >= players;
        }
    }

    private static final int WAIT_TIME = 2000;
    private List<Bucket<T>> buckets;
    private int n;
    private int step;

    public RankedQueue() {
        super();
        defaultBuckets();
        Thread.ofVirtual().start(this::updateBuckets);
    }

    private void updateBuckets() {
        while (true) {
            try {
                Thread.sleep(WAIT_TIME);
            } catch (InterruptedException e) {
                System.out.println("RankedQueue updateBuckets interrupted");
            }

            if (this.step >= 1000 || this.getPlayers() <= 1) {
                System.out.println("Max step reached or not enough players in queue, not updating buckets");
                continue;
            }

            System.out.println("Updating buckets");

            this.step += this.step / 4;

            List<Bucket<T>> newBuckets = this.makeBukets();
            this.transferPlayers(this.buckets, newBuckets);

            this.buckets = newBuckets;
        }
    }

    private void defaultBuckets() {
        this.n = 10;
        this.step = 200;
        this.buckets = makeBukets();
    }

    private List<Bucket<T>> makeBukets() {
        List<Bucket<T>> b = new ArrayList<>();

        for (int i = 0; i < this.n; i++) {
            if (i < this.n - 1) {
                b.add(new Bucket<>(i * this.step, (i + 1) * this.step - 1));
            } else {
                // guarantee that the LGMs also have a bucket
                b.add(new Bucket<>((this.n - 1) * this.step, Integer.MAX_VALUE));
            }
        }

        return b;
    }

    private void transferPlayers(List<Bucket<T>> from, List<Bucket<T>> to) {
        for (Bucket<T> bucket : from) {
            for (T player : bucket.players) {
                for (Bucket<T> newBucket : to) {
                    if (newBucket.add(player)) {
                        break;
                    }
                }
            }
        }
    }

    @Override
    public boolean add(T x) {
        this.lock.lock();
        try {
            return this.buckets.stream().anyMatch(bucket -> bucket.add(x));
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public boolean remove(T x) {
        this.lock.lock();
        try {
            return this.buckets.stream().anyMatch(bucket -> bucket.remove(x));
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public boolean contains(T x) {
        this.lock.lock();
        try {
            return this.buckets.stream().anyMatch(bucket -> bucket.contains(x));
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public int getPlayers() {
        this.lock.lock();
        try {
            return this.buckets.stream().mapToInt(Bucket::size).sum();
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public boolean canStartGame(int players) {
        this.lock.lock();
        try {
            return this.buckets.stream().anyMatch(bucket -> bucket.canStartGame(players));
        } finally {
            this.lock.unlock();
        }
    }

    public List<T> takeGamePlayers(int players) {
        this.lock.lock();
        try {
            List<T> gamePlayers = new ArrayList<>();
            for (Bucket<T> bucket : this.buckets) {
                if (bucket.size() < players) {
                    continue;
                }

                for (T player : bucket.players) {
                    gamePlayers.add(player);
                    if (gamePlayers.size() == players) {
                        break;
                    }
                }

                for (T player : gamePlayers) {
                    bucket.remove(player);
                }

                List<Bucket<T>> bucketsCopy = new ArrayList<>(this.buckets);
                this.defaultBuckets();
                this.transferPlayers(bucketsCopy, this.buckets);

                break;
            }
            return gamePlayers;
        } finally {
            this.lock.unlock();
        }
    }
}
