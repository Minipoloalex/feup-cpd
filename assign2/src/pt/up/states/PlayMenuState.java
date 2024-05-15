package pt.up.states;

import java.io.PrintWriter;
import pt.up.User;

public class PlayMenuState extends State {
    public PlayMenuState(PrintWriter out) {
        super(out);
    }

    public PlayMenuState(PrintWriter out, User u) {
        super(out, u);
    }

    public State handle(String inputLine) {
        if (inputLine.equals("1")) {
            return new PlayMenuState(this.out, this.user);
        } else if (inputLine.equals("2")) {
            // return new RankState(out);
        }
        return null;
    }

    public void render() {
        out.println("Select the game mode you want to play");
        out.println("1. Simple");
        out.println("2. Rank");
    }

    public void onEnter() {
        out.println("Welcome to the game");
    }

    @Override
    public void onExit() {
    }
}
