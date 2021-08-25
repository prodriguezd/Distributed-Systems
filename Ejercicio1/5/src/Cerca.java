public class Cerca extends Thread {

    private int numThread;
    private int aBuscar;
    private int startIndex;
    private int finalIndex;
    private int position;
    private long elapsedTime;
    private boolean done;

    public Cerca(int numThread, int aBuscar, int startIndex, int finalIndex) {
        this.numThread = numThread;
        this.aBuscar = aBuscar;
        this.startIndex = startIndex;
        this.finalIndex = finalIndex;
        this.position = -1;
        this.done = false;
    }

    public int getNumThread() {
        return numThread;
    }

    public int getPosition() {
        return position;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getFinalIndex() {
        return finalIndex;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void run() {
        long startTime = System.nanoTime();

        for (int i = startIndex; i < finalIndex; i++) {
            if (Main.array[i] == aBuscar) {
                this.position = i;
                long stopTime = System.nanoTime();
                this.elapsedTime = stopTime - startTime;
                break;
            }
        }

        this.done = true;
    }
}
