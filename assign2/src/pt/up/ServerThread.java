package pt.up;

import java.net.*;

import pt.up.states.AuthMenuState;
import pt.up.states.State;

import java.io.*;

public class ServerThread implements Runnable {
    private final Socket clientSocket;
    private State state;

    public ServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    private void changeState(State st) {
        this.state.onExit();
        this.state = st;
        this.state.onEnter();
    }

    /**
     * Sends an empty line to the client.
     * The client expects a sequence of lines ending with an empty line, after which it will send its own input.
     * @param out
     */
    private void sendEmptyLine(PrintWriter out) {
        out.println();
    }
    @Override
    public void run() {
        try (
                PrintWriter out = new PrintWriter(this.clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));) {
            String inputLine;
            this.state = new AuthMenuState(out);
            this.state.onEnter();
            this.state.render();
            this.sendEmptyLine(out);

            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                State newState = this.state.handle(inputLine);
                if (newState != null) {
                    this.changeState(newState);
                }

                this.state.render();
                this.sendEmptyLine(out);
            }
        } catch (IOException e) {
            System.err.println("Exception caught when trying to listen on port "
                    + clientSocket.getPort() + " or listening for a connection");
            System.err.println(e.getMessage());
        }
    }
}
