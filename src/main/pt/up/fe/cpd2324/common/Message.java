package pt.up.fe.cpd2324.common;

// Represents a message that can be sent between the client and server
public class Message {
    public enum Type { 
        PLAIN,
        OK,
        ERROR,
        PING,
        SHOW,
        PROMPT,
        USERNAME,
        PASSWORD,
        MODE,
        GAME,
        CLEAR,
        WAIT,
        TIMEOUT,
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
        try {
            String[] parts = message.split(": ", 2);
            return new Message(Type.valueOf(parts[0]), parts[1]);
        } catch (NullPointerException e) {
            System.out.println("Error parsing message: " + e.getMessage());
        }

        return null;
    }
        
    @Override
    public String toString() {
        return this.type + ": " + this.content;
    }
}
