package ricartandgrawala;

import Main.Connection;
import Main.Main;
import Main.Message;
import java.util.LinkedList;

import static java.lang.Thread.sleep;

public class Lightweight {

    private int myId;
    private int myPort;
    private Connection conn;

    private LamportClock c;
    private int myts;
    private int numOk;
    private LinkedList<Integer> pendingQueueId;
    private LinkedList<Integer> pendingQueueTimestamp;
    private int heavyPort;

    public Lightweight(int myId, int myPort, int timestamp, int heavyPort) {
        numOk = 0;
        this.myId = myId;
        this.myPort = myPort;
        this.heavyPort = heavyPort;
        c = new LamportClock();
        pendingQueueId = new LinkedList<>();
        pendingQueueTimestamp = new LinkedList<>();
        myts = Integer.MAX_VALUE;
        c.setCount(timestamp);

        conn = new Connection(this.myId, this.myPort);
    }

    public void doWork() {
        //avisamos que estamos dentro
        int[] heavyweight = getHeavyweight();
        //Message message = new Message(heavyweight[1], heavyweight[0], myId, myPort, 0, "Hello");
        //conn.sendMessage(message);
        Message message;
        int destId;

        if (myId  == Main.LIGHTWEIGHT_A1_ID || myId  == Main.LIGHTWEIGHT_A2_ID || myId  == Main.LIGHTWEIGHT_A3_ID) destId = Main.HEAVYWEIGHT_A_ID;
        else destId = Main.HEAVYWEIGHT_B_ID;

        message = new Message(destId, heavyPort, myId, myPort, 0, "Hello");
        conn.sendMessage(message);

        while(true) {
            do {
                message = conn.receiveMessage();
                //msg = (String)conn.receiveMessage();
                handleMessage(message);
            }
            while(!message.getMessage().equals("I have token"));

            requestCS();

            for (int i = 0; i < 4; i++) {
                System.out.println("Sóc el procés lightweight " + name());
                Message msg = new Message(destId, heavyPort, myId, myPort, 0, conn.nameById());
                conn.sendMessage(msg);
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println();
            //conn.sendMessage(Main.HEAVYWEIGHT_B_PORT, "Done");
            message = new Message(heavyweight[1], heavyweight[0], myId, myPort, 0, "Done");
            conn.sendMessage(message);
            releaseCS();

        }

    }

    private void requestCS() {
        c.tick();
        myts = c.getValue();
        Message message = new Message(0, 0, myId, myPort, myts, "Request");
        conn.broadcast(message);

        numOk = 0;

        while (numOk < Main.MAX_RICARTAGRAWALA - 1) {
            message = conn.receiveMessage();
            handleMessage(message);
        }
    }

    private void releaseCS() {
        myts = Integer.MAX_VALUE;

        while (!pendingQueueId.isEmpty()) {
            int id = pendingQueueId.poll();
            Message message = new Message(id, getPort(id), myId, myPort, c.getValue(), "Okay");
            conn.sendMessage(message);
            pendingQueueTimestamp.poll();
        }
    }

    private void handleMessage(Message message) {
        if (!message.getMessage().equals("I have token")) {
            c.receiveAction(message.getSrcId(), message.getTimestamp());
            if (message.getMessage().equals("Request")) {
                if ((myts == Integer.MAX_VALUE) || (message.getTimestamp() < myts) || ((message.getTimestamp() == myts) && (message.getSrcId() < myId))) {
                    //send okay
                    Message msg = new Message(message.getSrcId(), message.getSrcPort(), myId, myPort, c.getValue(), "Okay");
                    conn.sendMessage(msg);
                }
                else {
                    pendingQueueTimestamp.add(message.getTimestamp());
                    pendingQueueId.add(message.getSrcId());
                }
            }
            else if (message.getMessage().equals("Okay")) {
                numOk++;
                //??
            }

        }
    }

    private int[] getHeavyweight() {
        switch (myId) {
            case Main.LIGHTWEIGHT_A1_ID:
                return new int[] {Main.HEAVYWEIGHT_A_PORT, Main.HEAVYWEIGHT_A_ID};

            case Main.LIGHTWEIGHT_A2_ID:
                return new int[] {Main.HEAVYWEIGHT_A_PORT, Main.HEAVYWEIGHT_A_ID};

            case Main.LIGHTWEIGHT_A3_ID:
                return new int[] {Main.HEAVYWEIGHT_A_PORT, Main.HEAVYWEIGHT_A_ID};

            case Main.LIGHTWEIGHT_B1_ID:
                return new int[] {Main.HEAVYWEIGHT_B_PORT, Main.HEAVYWEIGHT_B_ID};

            case Main.LIGHTWEIGHT_B2_ID:
                return new int[] {Main.HEAVYWEIGHT_B_PORT, Main.HEAVYWEIGHT_B_ID};
        }
        return null;
    }

    private String name() {

        switch (myId) {
            case Main.HEAVYWEIGHT_A_ID:
                return "HEAVYWEIGHT_A";
            case Main.HEAVYWEIGHT_B_ID:
                return "HEAVYWEIGHT_B";
            case Main.LIGHTWEIGHT_A1_ID:
                return "LIGHTWEIGHT_A1";
            case Main.LIGHTWEIGHT_A2_ID:
                return "LIGHTWEIGHT_A2";
            case Main.LIGHTWEIGHT_A3_ID:
                return "LIGHTWEIGHT_A3";
            case Main.LIGHTWEIGHT_B1_ID:
                return "LIGHTWEIGHT_B1";
            case Main.LIGHTWEIGHT_B2_ID:
                return "LIGHTWEIGHT_B2";
        }

        return "";
    }
    private int getPort(int id) {
        switch (id) {
            case Main.HEAVYWEIGHT_A_ID:
                return Main.HEAVYWEIGHT_A_PORT;
            case Main.HEAVYWEIGHT_B_ID:
                return Main.HEAVYWEIGHT_B_PORT;
            case Main.LIGHTWEIGHT_A1_ID:
                return Main.LIGHTWEIGHT_A1_PORT;
            case Main.LIGHTWEIGHT_A2_ID:
                return Main.LIGHTWEIGHT_A2_PORT;
            case Main.LIGHTWEIGHT_A3_ID:
                return Main.LIGHTWEIGHT_A3_PORT;
            case Main.LIGHTWEIGHT_B1_ID:
                return Main.LIGHTWEIGHT_B1_PORT;
            case Main.LIGHTWEIGHT_B2_ID:
                return Main.LIGHTWEIGHT_B2_PORT;
        }
        return 0;
    }

}
