package highest.pure.mina.protocol;

public enum MessageType {

    NONE(0),
    HEARTBEAT(1),
    COMMAND(2);

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
        }
        return NONE;
    }
}
