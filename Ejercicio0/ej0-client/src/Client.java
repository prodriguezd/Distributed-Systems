/*import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class Client {
    private int value;
    private int parentPort;
    private int type;
    private boolean end;
    private Socket socket;


    public Client(int value, int parentPort) { this.parentPort = parentPort; this.value = value; }

    public Client(int type, boolean end) { this.value = 0; this.parentPort = 0; this.type = type; this.end = end; }


    public void connect() throws IOException, ClassNotFoundException, InterruptedException {
        Socket central = new Socket("127.0.0.1", 9875);

        ObjectInputStream ois = new ObjectInputStream(central.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(central.getOutputStream());


        if (!this.end) {
            oos.writeObject("hello");
        }
        else oos.writeObject("end");

        this.parentPort = (Integer) ois.readObject();
        System.out.println("nos hemos de conectar al puerto: " + parentPort);

        central.close();

        run();

        System.out.println("finished!");
    }

    private void updateCurrentValue(int newValue) throws IOException {


        this.socket = new Socket("127.0.0.1", this.parentPort);
        ObjectOutputStream oos = new ObjectOutputStream(this.socket.getOutputStream());

        this.value = newValue;

        oos.flush();
        oos.writeObject(this.value);

    }

    private void getCurrentValue() throws IOException, ClassNotFoundException {

        this.socket = new Socket("127.0.0.1", this.parentPort);
        ObjectOutputStream oos = new ObjectOutputStream(this.socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());

        oos.flush();

        oos.writeObject("G");

        this.value = (Integer)ois.readObject();
    }

    private void run() throws InterruptedException, IOException, ClassNotFoundException {


        for (int i = 0; i < 10; i++) {

            getCurrentValue();

            System.out.println("Current value: " + this.value);

            if (this.type == 1) {

                updateCurrentValue(this.value + 1);
                    System.out.println("value updated");

            }

            sleep(1000);
        }


        this.socket.close();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getParentPort() {
        return parentPort;
    }

    public void setParentPort(int parentPort) {
        this.parentPort = parentPort;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

}
*/
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class Client {
    private int value;
    private int parentPort;
    private int type;
    private boolean end;
    private Socket socket;


    public Client(int value, int parentPort) { this.parentPort = parentPort; this.value = value; }

    public Client(int type, boolean end) { this.value = 0; this.parentPort = 0; this.type = type; this.end = end; }


    public void connect() throws IOException, ClassNotFoundException, InterruptedException {
        Socket central = new Socket("127.0.0.1", 9875);

        ObjectInputStream ois = new ObjectInputStream(central.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(central.getOutputStream());


        if (!this.end) {
            oos.writeObject("hello");
        }
        else oos.writeObject("end");

        this.parentPort = (Integer) ois.readObject();
        System.out.println("nos hemos de conectar al puerto: " + parentPort);

        central.close();

        run();

        System.out.println("finished!");
    }

    private void updateCurrentValue(int newValue) throws IOException, ClassNotFoundException {

        this.socket = new Socket("127.0.0.1", this.parentPort);
        ObjectOutputStream oos = new ObjectOutputStream(this.socket.getOutputStream());

        this.value = newValue;

        oos.flush();
        oos.writeObject(this.value);

        oos.close();

    }

    private void setUnOccupped() throws IOException {
        this.socket = new Socket("127.0.0.1", this.parentPort);
        ObjectOutputStream oos = new ObjectOutputStream(this.socket.getOutputStream());
        oos.flush();

        oos.writeObject("unset");
        oos.close();
    }

    private void setOccuped() throws IOException {
        this.socket = new Socket("127.0.0.1", this.parentPort);
        ObjectOutputStream oos = new ObjectOutputStream(this.socket.getOutputStream());
        oos.flush();

        oos.writeObject("set");
        oos.close();
    }

    private int lookOccuped() throws IOException, ClassNotFoundException {
        this.socket = new Socket("127.0.0.1", this.parentPort);
        ObjectOutputStream oos = new ObjectOutputStream(this.socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());

        oos.flush();

        oos.writeObject("look");


        int a =  (Integer)ois.readObject();

        oos.close();
        ois.close();

        return a;
    }

    private void getCurrentValue() throws IOException, ClassNotFoundException {

        this.socket = new Socket("127.0.0.1", this.parentPort);
        ObjectOutputStream oos = new ObjectOutputStream(this.socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());

        oos.flush();

        oos.writeObject("G");

        this.value = (Integer)ois.readObject();

        ois.close();
        oos.close();
    }

    private void run() throws InterruptedException, IOException, ClassNotFoundException {


        for (int i = 0; i < 10; i++) {

            while(lookOccuped() == 1){
                //wait...
            }

            setOccuped();

            getCurrentValue();

            System.out.println("Current value: " + this.value);

            if (this.type == 1) {

                updateCurrentValue(this.value + 1);
                System.out.println("value updated");

            }
            setUnOccupped();

            sleep(1000);
        }


        this.socket.close();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getParentPort() {
        return parentPort;
    }

    public void setParentPort(int parentPort) {
        this.parentPort = parentPort;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

}
