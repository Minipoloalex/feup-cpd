package pt.up.states;

import java.io.PrintWriter;

public class MenuState extends State {
    public MenuState(PrintWriter out) {
        super(out);
    }

    public State handle(String inputLine) {
        if (inputLine.equals("1")) {
            // return new SimpleState(out);
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
