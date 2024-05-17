import javax.net.ssl.SSLSocket;

public class ClientHandler implements Runnable {
    private final SSLSocket socket;
    private final Database database = Database.getInstance();

    /**
     * Constructor for the ClientHandler class.
     * 
     * @param socket The socket of the client.
     */
    public ClientHandler(SSLSocket socket) {
        this.socket = socket;
    }

    /**
     * Runs the client handler.
     */
    @Override
    public void run() {
        try {
            while (true) {
                String message = Connection.receive(this.socket);
                System.out.println("Received message: " + message);

                String[] parts = message.split(";");
                String option = parts[0];
                String username = parts[1];
                String password = parts[2];
                
                if (option.equals("register")) {
                    if (this.database.addPlayer(username, password)) {
                        Connection.send(this.socket, "Success");
                    } else {
                        Connection.send(this.socket, "Player already exists!");
                    }
                } else if (option.equals("login")) {
                    if (this.database.checkPassword(username, password)) {
                        Connection.send(this.socket, "Success");
                    } else {
                        Connection.send(this.socket, "Player not found!");
                    }
                } else if (option.equals("exit")) {
                    Connection.send(this.socket, "Goodbye!");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connection.close(this.socket);
        }

        System.out.println("Client disconnected: " + this.socket.getInetAddress().getHostAddress());

        Thread.currentThread().interrupt();
    }
}
