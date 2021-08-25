import java.util.LinkedList;

public class Cerca {
    static LinkedList<Integer> list = new LinkedList<>();
    int mida;
    int aBuscar;

    public Cerca(int mida, int aBuscar) {
        this.mida = mida;
        this.aBuscar = aBuscar;

        for (int i = 0; i < mida; i++) {
            list.add(i, i);
        }
    }

    void fromTop() {

        int i = 0;

        while (i < this.mida) {
            if (list.get(i) == this.aBuscar) {
                System.out.println("thread 1 ha encontrado el numero");
                break;
            }
            i++;
        }
    }

    void fromBottom() {
        int j = this.mida - 1;

        while (j >= 0) {
            if (list.get(j) == this.aBuscar) {
                System.out.println("thread 2 ha encontrado el numero");
                break;
            }
            j--;
        }
    }
}
