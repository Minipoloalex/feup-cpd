package pt.up;

public enum MessageType {
    PING, OK, ERROR, REGISTER, LOGIN, LOGOUT, NORMAL, RANKED;

    public static MessageType fromString(String message) {
        switch (message) {
            case "PING" -> {
                return PING;
            }
            case "OK" -> {
                return OK;
            }
            case "ERROR" -> {
                return ERROR;
            }
            case "REGISTER" -> {
                return REGISTER;
            }
            case "LOGIN" -> {
                return LOGIN;
            }
            case "LOGOUT" -> {
                return LOGOUT;
            }
            case "NORMAL" -> {
                return NORMAL;
            }
            case "RANKED" -> {
                return RANKED;
            }
            default -> throw new IllegalArgumentException("Invalid message: " + message);
        }
    }
}