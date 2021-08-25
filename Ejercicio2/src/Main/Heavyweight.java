package Main;

import static Main.Main.HEAVYWEIGHT_A_ID;
import static Main.Main.HEAVYWEIGHT_B_ID;

public class Heavyweight implements Runnable{

    private final int MAX_PROCESOS;
    private Connection conn;
    private int myId;
    private int myPort;
    private int otherHeavyweightId;
    private int otherHeavyweightPort;
    private boolean token;

    public Heavyweight(int myPort, int myId, int MAX_PROCESOS) {
        this.MAX_PROCESOS = MAX_PROCESOS;
        conn = new Connection(myId, myPort);
        this.myId = myId;
        this.myPort = myPort;

        //el proceso A empieza con el token
        if (myId == Main.HEAVYWEIGHT_B_ID) {
            token = true;
            otherHeavyweightId = HEAVYWEIGHT_A_ID;
            otherHeavyweightPort = Main.HEAVYWEIGHT_A_PORT;
        }
        else {
            token = false;
            otherHeavyweightId = HEAVYWEIGHT_B_ID;
            otherHeavyweightPort = Main.HEAVYWEIGHT_B_PORT;
        }
    }

    @Override
    public void run() {

        int conected = 0;
        while (conected < MAX_PROCESOS) {
            //String msg = (String)conn.receiveMessage();
            Message msg = conn.receiveMessage();

            if (msg.getMessage().equals("Hello")) {
                conected++;
            }
        }

        System.out.println("Todo los light conectados!");

        while (true) {
            while (!token) {
                //String msg = (String)conn.receiveMessage();
                Message message = conn.receiveMessage();

                if (message.getMessage().equals("Give token")) {
                    message = new Message(otherHeavyweightId, otherHeavyweightPort, myId, myPort, 0, "Token recibido");
                    //conn.sendMessage(otherHeavyweightPort, "Token recibido");
                    System.out.println("Soc el procés ["+  nameById() +"] i rebo token");
                    conn.sendMessage(message);

                    //ya tenemos el token.
                    token = true;
                }
            }
            //significa que se tiene el token. Hay que avisar a los lightweight que lo tenemos
            //broadcast a lighweight
            Message message = new Message(0,0, myId, myPort, 0, "I have token");
            conn.sendActionToLightweight(message);
            int answers = 0;
            int max;
            if (myId == HEAVYWEIGHT_B_ID) max = 2;
            else max = 3;


            while (answers < max) {
                message = conn.receiveMessage();

                if (message.getMessage().equals("Done")) {
                    answers++;
                    System.out.println("\n");
                }else{
                    System.out.println("Sóc el procés lightweight " + message.getMessage());
                }
            }

            //una vez hayan printado los procesoso por pantalla, dar el token al otro heavyweight
            message = new Message(otherHeavyweightId, otherHeavyweightPort, myId, myPort, 0, "Give token");
            conn.sendMessage(message);
            System.out.println("Soc el procés ["+  nameById() +"] i envio token");
            do {
                message = conn.receiveMessage();
            } while (!message.getMessage().equals("Token recibido"));

            token = false;
        }
    }

    public String nameById() {

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

}
