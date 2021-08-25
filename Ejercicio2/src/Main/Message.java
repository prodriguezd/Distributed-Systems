package Main;

import java.io.Serializable;

public class Message implements Serializable {
    private int destId;
    private int destPort;
    private int srcId;
    private int srcPort;
    private int timestamp;
    private String message;

    public Message(int destId, int destPort, int srcId, int srcPort, int timestamp, String message) {
        this.destId = destId;
        this.destPort = destPort;
        this.srcId = srcId;
        this.srcPort = srcPort;
        this.timestamp = timestamp;
        this.message = message;
    }

    public int getDestId() {
        return destId;
    }

    public int getDestPort() {
        return destPort;
    }

    public int getSrcId() {
        return srcId;
    }

    public int getSrcPort() {
        return srcPort;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setDestId(int destId) {
        this.destId = destId;
    }

    public void setDestPort(int destPort) {
        this.destPort = destPort;
    }
}
