package pt.up.fe.cpd2324.common;

// Represents a message that can be sent between the client and server
public class Message {
    public enum Type { 
        PLAIN,
        OK,
        ERROR,
        SHOW,
        PROMPT,
        USERNAME,
        PASSWORD,
        MODE,
        GAME,
        CLEAR,
        WAIT,
        GAME_OVER,
    } 

    private final Type type;
    private final String content;

    public Message(Type type, String content) {
        this.type = type;
        this.content = content;
    }

    public Type getType() {
        return this.type;
    }

    public String getContent() {
        return this.content;
    }

    public static Message fromString(String message) {
        String[] parts = message.split(": ", 2);
        return new Message(Type.valueOf(parts[0]), parts[1]);
    }

    @Override
    public String toString() {
        return this.type + ": " + this.content;
    }
}
