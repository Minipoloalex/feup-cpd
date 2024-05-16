package pt.up;

public class Player {
    private final String username;
    private final String token;
    private final Connection connection;

    public Player(String username, String token, Connection connection) {
        this.username = username;
        this.token = token;
        this.connection = connection;
    }

    public void sendGameConfirmation(String content) {
        connection.sendRequest(Message.confirm(content));
    }

    public boolean getGameConfirmation() {
        Message response = connection.receiveRequest();
        return response.isOk();
    }

    public void sendGameCanceled() {
        connection.sendRequest(Message.error("Game canceled"));
    }

    public void sendOk() {
        connection.sendRequest(Message.ok());
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }
}
