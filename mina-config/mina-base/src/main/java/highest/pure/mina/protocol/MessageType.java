package highest.pure.mina.protocol;

public enum MessageType {

    NONE(0),
    HEARTBEAT(1),
    CONFIGURATION(2),
    COMMAND(3);

    private int messageType = 0;

    MessageType(int type) {
        this.messageType = type;
    }

    public int getMessageType() {
        return this.messageType;
    }

    public static MessageType fromInt(int type) {
        if (type == HEARTBEAT.getMessageType()) {
            return HEARTBEAT;
        } else if (type == CONFIGURATION.getMessageType()) {
            return CONFIGURATION;
        } else if (type == COMMAND.getMessageType()) {
            return COMMAND;
        }
        return NONE;
    }
}
