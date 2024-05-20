package pt.up.fe.cpd2324.server;

import pt.up.fe.cpd2324.common.Connection;
import pt.up.fe.cpd2324.common.Message;
import pt.up.fe.cpd2324.common.TreeSet;
import pt.up.fe.cpd2324.client.Player;

import java.io.IOException;

import javax.net.ssl.SSLSocket;

// Authenticates the client and adds them to the list of authenticated players
public class ClientAuthenticator implements Runnable {
    private final SSLSocket clientSocket;

    private final TreeSet<Player> authenticatedPlayers;
    private final TreeSet<Player> availablePlayers;

    private final Database database = Database.getInstance();

    public ClientAuthenticator(SSLSocket socket, TreeSet<Player> authenticatedPlayers, TreeSet<Player> availablePlayers) {
        this.clientSocket = socket;
        this.authenticatedPlayers = authenticatedPlayers;
        this.availablePlayers = availablePlayers;
    }

    @Override
    public void run() {
        boolean authenticated = false;

        try {
            while (!authenticated) {
                String[] menu = {
                    " ______________________",
                    "|                      |",
                    "|  Authentication      |",
                    "|                      |",
                    "|  1. Login            |",
                    "|  2. Register         |",
                    "|                      |",
                    "|______________________|",
                };
                Connection.show(this.clientSocket, menu);
                
                Connection.prompt(this.clientSocket, "Option: ");
                String option = Connection.receive(this.clientSocket).getContent();

                if (!option.equals("1") && !option.equals("2")) {
                    Connection.error(this.clientSocket, "Invalid option!");
                    continue;
                }

                Connection.send(this.clientSocket, new Message(Message.Type.USERNAME, "Username: "));
                String username = Connection.receive(this.clientSocket).getContent();

                Connection.send(this.clientSocket, new Message(Message.Type.PASSWORD, "Password: "));
                String password = Connection.receive(this.clientSocket).getContent();

                switch (option) {
                    case "1":
                        authenticated = this.login(username, password);
                        break;
                    case "2":
                        authenticated = this.register(username, password);
                        break;
                };
            }   
        } catch (IOException e) {
            System.out.println("Error authenticating client: " + e.getMessage());
        }
    }

    private boolean login(String username, String password) throws IOException {
        if (this.database.checkPassword(username, password)) {
            Player player = this.database.getPlayer(username);
            if (this.authenticatedPlayers.contains(player)) {
                Connection.error(this.clientSocket, "Player already authenticated!");
                return false;
            }

            this.authenticatePlayer(player);

            Connection.ok(this.clientSocket, "Welcome back, " + username + "!");
            return true;
        } else {
            Connection.error(this.clientSocket, "Invalid username or password!");
            return false;
        }
    }

    private boolean register(String username, String password) throws IOException {
        if (this.database.addPlayer(username, password)) {
            Player player = this.database.getPlayer(username);
           
            this.authenticatePlayer(player);

            Connection.ok(this.clientSocket, "Welcome, " + username + "!");
            return true;
        } else {
            Connection.error(this.clientSocket, "Username already taken!");
            return false;
        }
    }

    private void authenticatePlayer(Player player) {
        player.setSocket(this.clientSocket);

        this.authenticatedPlayers.add(player);
        this.availablePlayers.add(player);

        System.out.println("Player " + player.getUsername() + " authenticated");
    }
}
