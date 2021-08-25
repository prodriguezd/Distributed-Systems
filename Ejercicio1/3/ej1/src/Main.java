import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("FAIL, arg num a buscar");
        }
        else {
            int aBuscar = Integer.parseInt(args[0]);

            final Cerca cerca = new Cerca(100, aBuscar);

            Thread th1 = new Thread() {
                @Override
                public void run() {
                    cerca.fromTop();
                }
            };

            Thread th2 = new Thread() {
                @Override
                public void run() {
                    cerca.fromBottom();
                }
            };

            th1.start();
            th2.start();
        }
    }
}
