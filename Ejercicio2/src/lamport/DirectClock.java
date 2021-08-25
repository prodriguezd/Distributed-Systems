package lamport;

import java.util.Arrays;

public class DirectClock {

    private int[] clock;
    int myId;

    public DirectClock(int numProc, int id) {
        myId = id;
        clock = new int[numProc];
        Arrays.fill(clock, 0);
        clock[myId] = 0;
    }

    public int getValue(int i) {
        return clock[i];
    }

    public void tick() {
        clock[myId]++;
    }

    public void sendAction() {
        //sentValue = clock[myId];
        tick();
    }

    public void receiveAction(int sender, int sentValue) {
        //actualizamos el clock del id origen y del actual (destino)
        clock[sender] = Math.max(clock[sender], sentValue);
        clock[myId] = Math.max(clock[myId], sentValue) + 1;
    }
}
