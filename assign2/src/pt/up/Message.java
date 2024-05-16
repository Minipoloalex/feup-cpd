package pt.up;

import java.util.ArrayList;
import java.util.Arrays;

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

    public static Message ok(String content) {
        return new Message(MessageType.OK, content);
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

    public static Message normal(String user, String token) {
        return new Message(MessageType.NORMAL, user + " " + token);
    }

    public static Message ranked(String user, String token) {
        return new Message(MessageType.RANKED, user + " " + token);
    }

    public static Message confirm(String content) {
        return new Message(MessageType.CONFIRM, content);
    }

    public boolean isOk() {
        return type == MessageType.OK;
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

    public ArrayList<String> getContentAsList() {
        return new ArrayList<>(Arrays.asList(content.split(" ")));
    }

    public static Message parse(String message) {
        String[] parts = message.split(" ", 2);
        MessageType type = MessageType.valueOf(parts[0]);
        String content = "";

        for (int i = 1; i < parts.length; i++) {
            content += parts[i];
            if (i < parts.length - 1) {
                content += " ";
            }
        }

        return new Message(type, content);
    }

    @Override
    public String toString() {
        return type + " " + content;
    }
}
