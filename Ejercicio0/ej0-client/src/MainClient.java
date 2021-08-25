import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainClient {
    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));

        String s = bufferRead.readLine();

        if (s != null/*rgs.length > 0*/) {
            Client client;
            switch(s/*args[0]*/) {
                case "read":
                    if (args.length > 1 && args[1].equals("end")) {
                        client = new Client(0, true);
                    }
                    else client = new Client(0, false);

                    client.connect();
                    break;
                case "readwrite":
                    System.out.println("end?");
                    s = bufferRead.readLine();
                    if (s.equals("yes")/*args.length > 1 && args[1].equals("end")*/) {
                        client = new Client(1, true);
                    }
                    else client = new Client(1, false);

                    client.connect();
                    break;
            }
        }
        else {
            System.out.println("FAIL");
        }
    }
}
