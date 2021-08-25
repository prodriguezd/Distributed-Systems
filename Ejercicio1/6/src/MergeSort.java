import java.util.Arrays;

public class MergeSort implements Runnable{

    private int[] array;
    private int left;
    private int right;
    private int[] answer;

    public MergeSort(int[] array, int left, int right) {
        this.array = array;
        this.left = left;
        this.right = right;
    }

    public int[] getArray() {
        return array;
    }

    public void setArray(int[] array) {
        this.array = array;
    }

    public int[] getAnswer() {
        return answer;
    }

    public void sort() {
        if (left == right && left >= 0) {
            this.answer = new int[]{array[left]};
            return;
        }

        if (left > right) return;


        int half1 = left + (right - left) / 2;
        int half2 = half1 + 1;

        MergeSort one = new MergeSort(array, left, half1);
        MergeSort two = new MergeSort(array, half2, right);

        Thread th1 = new Thread(one);
        Thread th2 = new Thread(two);

        th1.start();
        th2.start();


        try {
            th1.join();
            th2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        merge(one.answer, two.answer);

    }

    @Override
    public void run() {
        sort();
    }

    public void getSortedValues(int[] items)
    {

        for (int i = 0; i < items.length; i++)
            items[i] = answer[i];
    }

    public int[] merge(int[] array1, int[] array2) {
        answer = new int[array1.length + array2.length];

        int i=0, j=0;
        int l1, l2;
        l1 = array1.length;
        l2 = array2.length;

        int k = 0;

        while (i < l1 && j < l2) {
            if (array1[i] < array2[j]) answer[k++] = array1[i++];
            else if (array2[j] < array1[i]) answer[k++] = array2[j++];
            else answer[k++] = array1[i++];
        }

        while (i < l1) answer[k++] = array1[i++];

        while (j < l2) answer[k++] = array2[j++];

        return answer;
    }


    public void seqMergeSort(int[] array) {
        if (array == null) return;

        if (array.length > 1) {
            int mid = array.length / 2;

            int[] left = new int[mid];
            for (int i = 0; i < mid; i++) left[i] = array[i];

            int[] right = new int[array.length - mid];
            for (int i = mid; i < array.length; i++) right[i - mid] = array[i];

            seqMergeSort(left);
            seqMergeSort(right);

            int i = 0, j = 0, k= 0;

            while (i < left.length && j < right.length) {
                if (left[i] < right[j]) array[k++] = left[i++];
                else array[k++] = right[j++];
            }

            while (i < left.length) array[k++] = left[i++];
            while (j < right.length) array[k++] = right[j++];


        }
    }



}
