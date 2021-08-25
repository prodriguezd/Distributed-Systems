public class Main {

    static final int MIDA = 100;
    static int[] array;
    static int x = 0;

    public static void main (String[] args) throws InterruptedException {
        if (args.length < 2) System.out.println("FAIL, arg num a buscar i num threads");
        else {
            int aBuscar = Integer.parseInt(args[0]);
            int numThreads = Integer.parseInt(args[1]);

            array = new int[MIDA];

            for (int i = 0; i < array.length; i++) array[i] = i;

            final int[] index = chunkArray(numThreads);
            final Cerca[] cercas = new Cerca[numThreads];
            int j = 0;

            for (int i = 0; i < numThreads; i++) {
                cercas[i] = new Cerca(i + 1, aBuscar, index[j], index[j+1]);
                j+=2;
            }

            for (int i = 0; i < numThreads; i++) {
                cercas[i].start();
            }

            /*for (int i = 0; i < numThreads; i++) {
                cercas[i].join();
            }*/



            boolean running = true;
            boolean th = true;

            while (running) {
                for (int i = 0; i < numThreads; i++) {
                    th = th && cercas[i].isDone();
                }

                if (th == true) running = false;
                else th = true;
            }



            for (int i = 0; i < numThreads; i++) {
                if (cercas[i].getPosition() != -1) {
                    System.out.println("El thread " + cercas[i].getNumThread() + " ha trobat el numero");
                    System.out.println("El thread " + cercas[i].getNumThread() + " ha tardado " + (float)cercas[i].getElapsedTime() + " ns en realizar la búsqueda");
                    System.out.println("Posicion: " + cercas[i].getPosition());
                }
            }


        }

    }

    public static int calculateIndex() {
        return x++;
    }

    public static int[] chunkArray(int numThreads) {
        int[] output = new int[numThreads*2];
        int numOfChunks = MIDA/numThreads;

        int diferencia = MIDA - (numOfChunks * numThreads);
        int end = 0;
        int j = 0;

        for (int i = 0; i < numThreads - diferencia; i++) {
            int start = i * numOfChunks;
            output[j] = start;
            j++;
            int length = numOfChunks;
            end += length;
            output[j] = end;
            j++;
        }

        for (int i = numThreads - diferencia; i < numThreads; i++) {
            int start = end;
            output[j] = start;
            j++;
            int length = numOfChunks + 1;
            end += length;
            output[j] = end;
            j++;
        }
        return output;
    }
}
