package ricartandgrawala;

public class LamportClock {

    private int c;

    public LamportClock() {
        c = 1;
    }

    public int getValue() {
        return c;
    }

    public void tick() {
        c++;
    }

    public void sendAction() {
        //include c in message
        c++;
    }

    public void receiveAction(int src, int sentValue) {
        c = Math.max(c, sentValue) + 1;
    }

    public void setCount(int c) {
        this.c = c;
    }
}
