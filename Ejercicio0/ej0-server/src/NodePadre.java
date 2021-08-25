import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class NodePadre extends Thread{
    private int value;
    private ArrayList nodes;
    private int puertoPropio;
    private int portCentral;
    private int numHijos;
    private int name;
    private ServerSocket server   = null;
    private Socket socketCentral;

    public NodePadre(int port, int puerto) throws IOException {
        this.numHijos = numHijos;
        this.value = 0;
        this.portCentral = port;
        this.puertoPropio = puerto;
        this.name = puerto;
    }

    @Override
    public void run() {
        int occuped;
        try {
            //SERVER de los hijos
            this.server = new ServerSocket(this.puertoPropio);
            System.out.println("\tServer " + this.puertoPropio % 9880 + " started");

            while(true) {
                Socket clientSocket = server.accept();
                ObjectInputStream oisC = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream oosC = new ObjectOutputStream(clientSocket.getOutputStream());

                Object message = oisC.readObject();

                this.socketCentral = new Socket("127.0.0.1", this.portCentral);
                ObjectOutputStream oos = new ObjectOutputStream(socketCentral.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socketCentral.getInputStream());

                if (message.equals("look")) {
                    oos.flush();
                    oos.writeObject("look");
                    occuped = (Integer) ois.readObject();
                    oosC.writeObject(occuped);
                }else if (message.equals("set")) {
                    oos.flush();
                    oos.writeObject("set");
                }else if (message.equals("unset")) {
                    oos.flush();
                    oos.writeObject("unset");
                }else if ("G".equals(message)) {
                    //consultar valor
                    oos.flush();
                    oos.writeObject("G");
                    value = (Integer) ois.readObject();
                    oosC.writeObject(value);
                } else {
                    //modificar valor
                    value = (Integer) message;
                    oos.flush();
                    oos.writeObject(value);
                }
                System.out.println("\tClosing connection");
                oisC.close();
                oosC.close();
                clientSocket.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
