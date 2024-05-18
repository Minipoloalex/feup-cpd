package pt.up.fe.cpd2324.server;

import pt.up.fe.cpd2324.common.Connection;
import pt.up.fe.cpd2324.client.Player;

import java.util.Set;
import javax.net.ssl.SSLSocket;

public class ClientAuthenticator implements Runnable {
    private final SSLSocket clientSocket;

    private final Set<Player> authenticatedPlayers;
    private final Set<Player> availablePlayers;

    private final Database database = Database.getInstance();

    public ClientAuthenticator(SSLSocket socket, Set<Player> authenticatedPlayers, Set<Player> availablePlayers) {
        this.clientSocket = socket;
        this.authenticatedPlayers = authenticatedPlayers;
        this.availablePlayers = availablePlayers;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Connection.send(this.clientSocket, "Do you want to login or register? (login/register): ");
                String option = Connection.receive(this.clientSocket);
                Connection.send(this.clientSocket, "Enter your username: ");
                String username = Connection.receive(this.clientSocket);
                Connection.send(this.clientSocket, "Enter your password: ");
                String password = Connection.receive(this.clientSocket);

                // Check if the player is already authenticated
                if (this.authenticatedPlayers.stream().anyMatch(player -> player.getUsername().equals(username))) {
                    Connection.send(this.clientSocket, "Player already authenticated!");
                    continue;
                }

                if (option.equals("register")) {
                    if (this.database.addPlayer(username, password)) {
                        Connection.send(this.clientSocket, "OK");
                    } else {
                        Connection.send(this.clientSocket, "Username already taken!");
                        continue;
                    }
                } else if (option.equals("login")) {
                    if (this.database.checkPassword(username, password)) {
                        Connection.send(this.clientSocket, "OK");
                    } else {
                        Connection.send(this.clientSocket, "Invalid username or password!");
                        continue;
                    }
                } else {
                    Connection.send(this.clientSocket, "Invalid option!");
                    continue;
                }

                Player player = this.database.getPlayer(username);
                player.setSocket(this.clientSocket);
                this.authenticatedPlayers.add(player);
                this.availablePlayers.add(player);

                System.out.println("Player " + player.getUsername() + " has been authenticated.");

                break;
            }
        } catch (Exception e) {
            System.out.println("Error authenticating the client: " + e.getMessage());
        }
    }  
}
