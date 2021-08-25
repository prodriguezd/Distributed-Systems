package Main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection {

    public static String LOCALHOST = "127.0.0.1";

    private ServerSocket serverSocket;
    private int myId;
    private int myPort;

    public Connection(int myId, int myPort) {
        this.myId = myId;
        this.myPort = myPort;

        try {
            serverSocket = new ServerSocket(myPort);
        } catch (IOException e) {
            System.err.printf("Error al crear el socket de servidor [" + nameById() + "]\n");
            e.printStackTrace();
        }
    }

    public void sendMessage(Message msg) {

        try {
            Socket socket = new Socket(LOCALHOST, msg.getDestPort());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(msg);
            oos.close();
            socket.close();
        } catch (IOException e) {
            System.err.printf("Error enviar mensaje de [%d] a [%d]\n", myPort, msg.getDestPort());
            e.printStackTrace();
        }

    }

    public Message receiveMessage() {
        try {
            Socket socket = serverSocket.accept();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message message = (Message) ois.readObject();
            ois.close();
            socket.close();

            return message;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendActionToLightweight(Message msg) {
        for (int port : getDestinationsHeavyweight(myPort)) {
            msg.setDestPort(port);
            msg.setDestId(getId(port));
            sendMessage(msg);
        }
    }

    public void broadcast(Message msg) {
        for (int port : getDestinationsLightweight(myPort)) {
            msg.setDestPort(port);
            msg.setDestId(getId(port));
            sendMessage(msg);
        }
    }

    private int[] getDestinationsLightweight(int src) {
        switch (src) {
            case Main.LIGHTWEIGHT_A1_PORT:
                return new int[]{Main.LIGHTWEIGHT_A2_PORT, Main.LIGHTWEIGHT_A3_PORT};
            case Main.LIGHTWEIGHT_A2_PORT:
                return new int[]{Main.LIGHTWEIGHT_A1_PORT, Main.LIGHTWEIGHT_A3_PORT};
            case Main.LIGHTWEIGHT_A3_PORT:
                return new int[]{Main.LIGHTWEIGHT_A2_PORT, Main.LIGHTWEIGHT_A1_PORT};
            case Main.LIGHTWEIGHT_B1_PORT:
                return new int[]{Main.LIGHTWEIGHT_B2_PORT};
            case Main.LIGHTWEIGHT_B2_PORT:
                return new int[]{Main.LIGHTWEIGHT_B1_PORT};
        }
        return null;
    }

    private int[] getDestinationsHeavyweight(int src) {
        switch (src) {
            case Main.HEAVYWEIGHT_A_PORT:
                return new int[]{Main.LIGHTWEIGHT_A1_PORT, Main.LIGHTWEIGHT_A2_PORT, Main.LIGHTWEIGHT_A3_PORT};
            case Main.HEAVYWEIGHT_B_PORT:
                return new int[]{Main.LIGHTWEIGHT_B1_PORT, Main.LIGHTWEIGHT_B2_PORT};
        }
        return new int[0];
    }

    private int getId(int port) {
        switch (port) {
            case Main.HEAVYWEIGHT_A_PORT:
                return Main.HEAVYWEIGHT_A_ID;
            case Main.HEAVYWEIGHT_B_PORT:
                return Main.HEAVYWEIGHT_B_ID;
            case Main.LIGHTWEIGHT_A1_PORT:
                return Main.LIGHTWEIGHT_A1_ID;
            case Main.LIGHTWEIGHT_A2_PORT:
                return Main.LIGHTWEIGHT_A2_ID;
            case Main.LIGHTWEIGHT_A3_PORT:
                return Main.LIGHTWEIGHT_A3_ID;
            case Main.LIGHTWEIGHT_B1_PORT:
                return Main.LIGHTWEIGHT_B1_ID;
            case Main.LIGHTWEIGHT_B2_PORT:
                return Main.LIGHTWEIGHT_B2_ID;
        }
        return 0;
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