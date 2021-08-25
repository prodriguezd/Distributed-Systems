import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MainServer {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ServerSocket server   = null;
        int port = 9875;
        Scanner scan = new Scanner(System.in);
        int servers = 0;
        int numPadres = 0;
        int numHijos = 0;
        int valor = 0;
        int occuped = 0;

        ArrayList<Integer> referencia = new ArrayList<Integer>(Arrays.asList(1,5,12,24,40,60,84,112,144,180));
        ArrayList<Socket> socketsServers = new ArrayList<Socket>();
        ArrayList<Integer> puertos = new ArrayList<>(Arrays.asList(9880, 9881, 9882, 9883, 9884));
        ArrayList<ObjectOutputStream> oosString = new ArrayList<>();
        ArrayList<ObjectInputStream> oisString = new ArrayList<>();


        server = new ServerSocket(port);
        System.out.println("Server Central started");

        while(true){
            Socket socket = server.accept();
            System.out.println("server conectado!");
            socketsServers.add(servers, socket);
            //read from socket to ObjectInputStream object
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            String message = (String) ois.readObject();

            oosString.add(servers, oos);
            oisString.add(servers, ois);

            servers++;

            if(message.equals("end")){
                break;
            }
        }

        System.out.println("numero de servers total: " + servers);

        for (int i = 0; i < referencia.size(); i++){
            if(servers - referencia.get(i) < 0){
                if (i < 2) {
                    numPadres = 1;
                    numHijos = 2;
                }else {
                    numPadres = i;
                    numHijos = (numPadres - 1) * 2;
                }

               /* System.out.println("numPadres: " + numPadres);
                System.out.println("numHijos: " + numHijos);*/
                break;
            }
        }

        for (int i = 0; i < numPadres; i++){
            NodePadre hijo = new NodePadre(port, puertos.get(i));
            hijo.start();
        }

        int serv= 0;
        int fathers = 0;

        while (serv < servers){
            //ObjectInputStream ois = new ObjectInputStream(socketsServers.get(serv).getInputStream());
            //ObjectOutputStream oos = new ObjectOutputStream(socketsServers.get(serv).getOutputStream());

            if (fathers >= numPadres){
                fathers = 1;
            }else fathers++;

            oosString.get(serv).writeObject(puertos.get(fathers -1));

            oisString.get(serv).close();
            oosString.get(serv).close();
            socketsServers.get(serv).close();
            serv++;
        }

        while (true){
            Socket socket = server.accept();
            socket.setSoTimeout(5000);
            //read from socket to ObjectInputStream object
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            Object message = ois.readObject();

            if (message.equals("look")) {
                oos.flush();
                oos.writeObject(occuped);
            }else if (message.equals("set")) {
                occuped = 1;
            }else if (message.equals("unset")) {
                occuped = 0;
            }else if ("G".equals(message)) {
                oos.flush();
                oos.writeObject(valor);
                System.out.println("enviamos un: " + valor);
            } else {
                valor = (Integer) message;
                System.out.println("valor actualizado a: " + valor);
            }

            System.out.println("\tClosing connection");
            ois.close();
            oos.close();
            socket.close();
        }
    }
}
