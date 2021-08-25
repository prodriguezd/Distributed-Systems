public class Main {
    static final int MIDA = 100;
    public static void main(String[] args) throws InterruptedException {

        if (args.length < 2) System.out.println("FAIL, arg num a buscar i num threads");
        else {

            int aBuscar = Integer.parseInt(args[0]);
            int numThreads = Integer.parseInt(args[1]);
            int[] array = new int[MIDA];

            for (int i = 0; i < array.length; i++) {
                array[i] = i;
            }

            int casellaInicial = cercaParallela(aBuscar, array, numThreads);

            System.out.println("casilla inicial " + casellaInicial );
        }
    }

    public static int cercaParallela(int aBuscar, int[] array, int numThreads) throws InterruptedException {

        int[][] parts = chunkArray(array, numThreads);
        Cerca threads[] = new Cerca[numThreads];

        for (int i = 0; i < numThreads; i++) threads[i] = new Cerca(MIDA, i + 1, aBuscar, parts[i]);

        for (int i = 0; i < numThreads; i++) threads[i].start();
        //for (int i = 0; i < numThreads; i++) threads[i].join();

        boolean running = true;
        boolean th = true;

        while (running) {
            for (int i = 0; i < numThreads; i++) th = th && threads[i].getDone();

            if (th == true) running = false;
            else th = true;
        }

        for (int i = 0; i < numThreads; i++) {
            if (threads[i].getPosition() != -1) {
                System.out.println("El thread " + threads[i].getNumThread() + " ha trobat el numero");
                System.out.println("El thread " + threads[i].getNumThread() + " ha tardado " + (float)threads[i].getElapsedTime() + " ns en realizar la búsqueda");
                return calculaCasilla(threads, i);
            }
        }

        return -1;
    }

    private static int calculaCasilla(Cerca[] threads, int thread) {
        int casilla = 0;

        for (int i = 0; i < thread; i++) {
            casilla += threads[i].getArray().length;
        }

        for (int i = 0; i < threads[thread].getArray().length; i++) {
            casilla++;
            if (i == threads[thread].getPosition()) break;
        }
        return casilla - 1;
    }


    public static int[][] chunkArray(int[] array, int chunkSize) {

        int numOfChunks = MIDA/chunkSize;
        int[][] output = new int[chunkSize][];

        int diferencia = MIDA - (numOfChunks * chunkSize);
        int end = 0;

        for(int i = 0; i < chunkSize - diferencia; ++i) {
            int start = i * numOfChunks;
            int length = numOfChunks;
            end += length;
            int[] temp = new int[length];
            System.arraycopy(array, start, temp, 0, length);
            output[i] = temp;
        }

        for (int i = chunkSize - diferencia; i < chunkSize; i++) {
            int start = end;
            int length = numOfChunks + 1;
            end += length;
            int[] temp = new int[length];
            System.arraycopy(array, start, temp, 0, length);
            output[i] = temp;

        }
        return output;
    }
}