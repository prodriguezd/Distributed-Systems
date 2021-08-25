import java.util.Arrays;
import java.util.Random;

public class Main {
    static final int MIDA = 100000;

    public static void main(String[] args) {

        /*GENERATION OF RANDOM ARRAY*/
        int[] array = new int[MIDA];
        int[] array2 = new int[MIDA];
        Random rd = new Random();
        int x;
        for (int i = 0; i < MIDA; i++) {
            x = Math.abs(rd.nextInt() % 1000);
            array[i] = x;
            array2[i] = x;
        }
        System.out.println("Unsorted: " + Arrays.toString(array));

        MergeSort merge = new MergeSort(array, 0, array.length - 1);

        long start = System.currentTimeMillis();
        Thread main = new Thread(merge);
        main.start();

        try {
            main.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("Sorted multithread!  time in millis: " + (end - start));

        //sequencial

       /* start = System.currentTimeMillis();

        merge.seqMergeSort(array2);
        end = System.currentTimeMillis();

        System.out.println("Sorted! time in millis: " + (end - start));*/

    }



}
