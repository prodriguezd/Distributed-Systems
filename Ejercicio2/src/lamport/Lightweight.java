package lamport;

import Main.Connection;
import Main.Main;
import Main.Message;

import java.net.Socket;

import static Main.Main.MAX_LAMPORT;
import static java.lang.Thread.enumerate;
import static java.lang.Thread.sleep;

/*
•To request the critical section, a process sends a timestamped message to all other
processes and adds a timestamped request to the queue.
•On receiving a request message, the request and its timestamp are stored in the queue
and a timestamped acknowledgment is sent back.
•To release the critical section, a process sends a release message to all other processes.
•On receiving a release message, the corresponding request is deleted from the queue.
•A process determines that it can access the critical section if and only if (1) it has a request
in the queue with timestamp t,(2) t is less than all other requests in the queue, and (3) it has
received a niessage from every other process with timestamp greater than t (the request
acknowledgments ensure this).
 */

public class Lightweight {
    private int myId;
    private int myPort;
    private Connection connection;
    private DirectClock directClock;
    private int[] array; //colaRequests
    private int heavyPort;


    public Lightweight(int id, int port, int heavyPort) {
        this.myId = id;
        this.myPort = port;
        this.heavyPort = heavyPort;
        connection = new Connection(id, port);
        directClock = new DirectClock(MAX_LAMPORT, myId);
        array = new int[MAX_LAMPORT];

        for (int i = 0; i < MAX_LAMPORT; i++){
            array[i] = (int) Double.POSITIVE_INFINITY;
        }
    }

    public Lightweight(int id, int port, int heavyPort, int timestamp) {
        this.myId = id;
        this.myPort = port;
        this.heavyPort = heavyPort;
        connection = new Connection(id, port);
        directClock = new DirectClock(MAX_LAMPORT, myId);
        array = new int[MAX_LAMPORT];

        for (int i = 0; i < MAX_LAMPORT; i++){
            array[i] = (int) Double.POSITIVE_INFINITY;
        }

        for (int i = 0; i < timestamp; i++){
            directClock.tick();
        }
    }

    public void handleMsg(Message message){
        //si mensage != I have token, se tiene que hacer algo con el directClock
        if (!message.getMessage().equals("I have token")) {
            //actualiza clocks
            directClock.receiveAction(message.getSrcId(), message.getTimestamp());

            if (message.getMessage().equals("Request")) {
                //actualizamos el valor del id con el timestamp actual
                array[message.getSrcId()] = message.getTimestamp();

                //respondemos ACK
                Message msg = new Message(message.getSrcId(), message.getSrcPort(), myId, myPort, directClock.getValue(myId), "ACK");
                connection.sendMessage(msg);
            } else if (message.getMessage().equals("Release")) {
                //empezamos de nuevo = infinito
                array[message.getSrcId()] = (int) Double.POSITIVE_INFINITY;
            }
        }
    }



    public void goLight() {
        int[] heavyweight = getHeavyweight();
        //Message message = new Message(heavyweight[1], heavyweight[0], myId, myPort, 0, "Hello");
        //connection.sendMessage(message);
        int destId;
        int destPort;
        Message message2;
        Message message;

        if (myId  == Main.LIGHTWEIGHT_A1_ID || myId  == Main.LIGHTWEIGHT_A2_ID || myId  == Main.LIGHTWEIGHT_A3_ID) {
            destId = Main.HEAVYWEIGHT_A_ID;
            destPort = Main.HEAVYWEIGHT_A_PORT;
        } else {
            destId = Main.HEAVYWEIGHT_B_ID;
            destPort = Main.HEAVYWEIGHT_B_PORT;
        }

        message = new Message(destId, destPort, myId, myPort, 0, "Hello");
        connection.sendMessage(message);

        while (true) {
            do {
                //nos conectamos mediante socket y esperamos recibir el msg
                message = connection.receiveMessage();
                handleMsg(message);
            } while (!message.getMessage().equals("I have token"));

            //pedimos permiso para printar x pantalla
            requestCS();

            for (int i=0; i<4; i++){
                System.out.println("Sóc el procés lightweight " + connection.nameById());
                Message msg = new Message(destId, heavyPort, myId, myPort, 0, connection.nameById());
                connection.sendMessage(msg);
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println();
            message = new Message(heavyweight[1], heavyweight[0], myId, myPort, 0, "Done");
            connection.sendMessage(message);
            releaseCS();
        }
    }

    private int[] getHeavyweight() {
        switch (myId) {
            case Main.LIGHTWEIGHT_A1_ID, Main.LIGHTWEIGHT_A2_ID, Main.LIGHTWEIGHT_A3_ID:
                return new int[] {Main.HEAVYWEIGHT_A_PORT, Main.HEAVYWEIGHT_A_ID};


            case Main.LIGHTWEIGHT_B1_ID, Main.LIGHTWEIGHT_B2_ID:
                return new int[] {Main.HEAVYWEIGHT_B_PORT, Main.HEAVYWEIGHT_B_ID};

        }
        return null;
    }

    private void releaseCS() {
        array[myId] = (int) Double.POSITIVE_INFINITY;
        Message message = new Message(0, 0, myId, myPort, directClock.getValue(myId), "Release");
        connection.broadcast(message);
        //connection.sendActionToLightweight("release");
    }

    private void requestCS() {
        directClock.tick();
        array[myId] = directClock.getValue(myId);

        //se manda msg de request con el valor de mi clock
        Message message = new Message(0, 0, myId, myPort, array[myId], "Request");
        connection.broadcast(message);

        System.out.println("while okCS");
        while (!okayCS()) {
            //Podremos acceder a la seccion critica si:
            //  - hay request (timestamp no infinito)
            //  - mi timestamp es menor q todas las otras solicitudes
            //  - he recibido timestamp de todos mayor al mio (al recibir request se envia ack con mi timestamp)
            message = connection.receiveMessage();
            handleMsg(message);
        }
    }


    private boolean okayCS() {
        for (int i = 0; i < array.length; i++){
            if(isGreater(array[myId], myId, array[i], i)) {
                return false;
            }
            if (isGreater(array[myId], myId, directClock.getValue(i), i)){
                return false;
            }
        }
        return true;
    }

    private boolean isGreater(int i, int myId, int i2, int id2) {
        if(i2 == (int) Double.POSITIVE_INFINITY) return false;
        return ((i > i2) || ((i == i2) && (myId > id2)));
    }
}