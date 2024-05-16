package pt.up;

public class Message {
    private final MessageType type;
    private final String content;

    public Message(MessageType type, String content) {
        this.type = type;
        this.content = content;
    }

    public static Message ok() {
        return new Message(MessageType.OK, "");
    }

    public static Message error(String message) {
        return new Message(MessageType.ERROR, message);
    }

    public static Message ping() {
        return new Message(MessageType.PING, "");
    }

    public static Message register(String username, String password) {
        return new Message(MessageType.REGISTER, username + " " + password);
    }

    public static Message login(String username, String password) {
        return new Message(MessageType.LOGIN, username + " " + password);
    }

    public static Message logout() {
        return new Message(MessageType.LOGOUT, "");
    }

    public static Message normal(String message) {
        return new Message(MessageType.NORMAL, message);
    }

    public static Message ranked(String message) {
        return new Message(MessageType.RANKED, message);
    }

    public boolean isError() {
        return type == MessageType.ERROR;
    }

    public MessageType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public static Message parse(String message) {
        String[] parts = message.split(" ", 2);
        MessageType type = MessageType.valueOf(parts[0]);
        String content = "";

        for (int i = 1; i < parts.length; i++) {
            content += parts[i];
        }

        return new Message(type, content);
    }

    @Override
    public String toString() {
        return type + " " + content;
    }
}
