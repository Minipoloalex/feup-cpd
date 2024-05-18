import java.util.Set;
import javax.net.ssl.SSLSocket;

public class ClientAuthenticator implements Runnable {
    private final SSLSocket clientSocket;
    private final Set<Player> players;

    private final Database database = Database.getInstance();

    public ClientAuthenticator(SSLSocket socket, Set<Player> players) {
        this.clientSocket = socket;
        this.players = players;
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

                if (option.equals("register")) {
                    if (this.database.addPlayer(username, password)) {
                        Connection.send(this.clientSocket, "OK");
                    } else {
                        Connection.send(this.clientSocket, "Player already exists!");
                        continue;
                    }
                } else if (option.equals("login")) {
                    if (this.database.checkPassword(username, password)) {
                        Connection.send(this.clientSocket, "OK");
                    } else {
                        Connection.send(this.clientSocket, "Player not found!");
                        continue;
                    }
                } else {
                    Connection.send(this.clientSocket, "Invalid option!");
                    continue;
                }

                Player player = this.database.getPlayer(username);
                player.setSocket(this.clientSocket);
                this.players.add(player);

                System.out.println("Player " + player.getUsername() + " has been authenticated.");

                break;
            }
        } catch (Exception e) {
            System.out.println("Error authenticating the client: " + e.getMessage());
        }
    }  
}
