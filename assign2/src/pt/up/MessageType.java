package pt.up;

public enum MessageType {
    PING, OK, ERROR, REGISTER, LOGIN, LOGOUT, NORMAL, RANKED, ACCEPT, CONFIRM, LEAVE;

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
            case "ACCEPT" -> {
                return ACCEPT;
            }
            case "CONFIRM" -> {
                return CONFIRM;
            }
            case "LEAVE" -> {
                return LEAVE;
            }
            default -> throw new IllegalArgumentException("Invalid message: " + message);
        }
    }
}