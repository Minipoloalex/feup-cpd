package pt.up.states;

import pt.up.Queues.NormalQueue;
import pt.up.Queues.Queue;
import pt.up.User;

import java.io.PrintWriter;

public class QueueState extends State {
    static Queue queue = NormalQueue.getQueueObject();

    public QueueState(PrintWriter out, User u) {
        super(out, u);
    }

    @Override
    public State handle(String inputLine) {
        if (inputLine.equals("q")) {
            return new PlayMenuState(this.out, this.user);
        }
        return this;
    }

    @Override
    public void render() {

    }

    @Override
    public void onEnter() {
        if (queue.add(this.user)) {
            System.out.println("Added " + this.user + " to the queue");
            this.out.println("You have been added to the queue.");
            this.out.println("Enter 'q' to quit");
        } else {
            System.out.println("Failed to add " + this.user + " to the queue");
            this.out.println("You have not been added to the queue.");
        }
    }

    @Override
    public void onExit() {
        System.out.println("Exiting Normal Queue.");
    }
}
