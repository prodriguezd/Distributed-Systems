package Main;


import lamport.Lightweight;

public class Main {

    public final static int MAX_LAMPORT = 3;
    public final static int MAX_RICARTAGRAWALA = 2;

    public final static int HEAVYWEIGHT_A_ID = 10;
    public final static int HEAVYWEIGHT_B_ID = 20;
    public final static int LIGHTWEIGHT_A1_ID = 0;
    public final static int LIGHTWEIGHT_A2_ID = 1;
    public final static int LIGHTWEIGHT_A3_ID = 2;
    public final static int LIGHTWEIGHT_B1_ID = 3;
    public final static int LIGHTWEIGHT_B2_ID = 4;

    public final static int HEAVYWEIGHT_A_PORT = 1011;
    public final static int HEAVYWEIGHT_B_PORT = 1012;
    public final static int LIGHTWEIGHT_A1_PORT = 1013;
    public final static int LIGHTWEIGHT_A2_PORT = 1014;
    public final static int LIGHTWEIGHT_A3_PORT = 1015;
    public final static int LIGHTWEIGHT_B1_PORT = 1016;
    public final static int LIGHTWEIGHT_B2_PORT = 1017;

    public static void main(String[] args) {
        System.out.flush();

        if (args.length == 0) {
            // lanzar procesos heavyweightA y heavywheightB
            Thread A = new Thread(new Heavyweight(HEAVYWEIGHT_A_PORT, HEAVYWEIGHT_A_ID, MAX_LAMPORT));
            Thread B = new Thread(new Heavyweight(HEAVYWEIGHT_B_PORT, HEAVYWEIGHT_B_ID, MAX_RICARTAGRAWALA));

            A.start();
            B.start();
        }
        else {
            switch (args[0]) {
                case "A1":
                    /*Lightweight lightweight = new Lightweight(LIGHTWEIGHT_A1_ID, LIGHTWEIGHT_A1_PORT);
                    lightweight.goLight();*/
                    new lamport.Lightweight(LIGHTWEIGHT_A1_ID, LIGHTWEIGHT_A1_PORT, HEAVYWEIGHT_A_PORT,7).goLight();
                    break;
                case "A2":
                    new lamport.Lightweight(LIGHTWEIGHT_A2_ID, LIGHTWEIGHT_A2_PORT, HEAVYWEIGHT_A_PORT,2).goLight();
                    break;
                case "A3":
                    new lamport.Lightweight(LIGHTWEIGHT_A3_ID, LIGHTWEIGHT_A3_PORT, HEAVYWEIGHT_A_PORT, 8).goLight();
                    break;
                case "B1":
                    new ricartandgrawala.Lightweight(LIGHTWEIGHT_B1_ID, LIGHTWEIGHT_B1_PORT, 7, HEAVYWEIGHT_B_PORT).doWork();
                    break;
                case "B2":
                    new ricartandgrawala.Lightweight(LIGHTWEIGHT_B2_ID, LIGHTWEIGHT_B2_PORT, 8, HEAVYWEIGHT_B_PORT).doWork();
                    break;
            }
        }
    }
}
