public class Cerca extends Thread {

    private int mida;
    private int numThread;
    private int aBuscar;
    private int position;
    private int[] array;
    private boolean done;
    private long elapsedTime;

    public Cerca (int mida, int numThread, int aBuscar, int[] array) {
        this.mida = mida;
        this.numThread = numThread;
        this.aBuscar = aBuscar;
        this.array = array;
        this.position = -1;
        this.done = false;
    }

    public int getNumThread() {
        return numThread;
    }

    public int getPosition() {
        return position;
    }

    public int[] getArray() {
        return array;
    }

    public boolean getDone() {
        return done;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void run() {
        long startTime = System.nanoTime();
        for (int i = 0; i < this.array.length; i++) {
            if (this.array[i] == aBuscar) {
                this.position = i;
                long stopTime = System.nanoTime();
                this.elapsedTime = stopTime - startTime;
                break;
            }
        }

        //System.out.println(elapsedTime);
        this.done = true;
    }
}
