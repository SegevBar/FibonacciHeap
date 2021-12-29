import java.util.ArrayList;
import java.util.Collections;

public class RunQ2 {

    public static void main(String[] arg) {
        fillChartTest();
    }

    public static void fillChart() {
        FibonacciHeapQ2 H = new FibonacciHeapQ2();
        int[] toInsert = {728,6560,59048,531440,4782968};
        int[] toDelete = {546,4920,44286,398580,3587226};

        for (int j = 0; j < toInsert.length; j++) {
            long start = System.currentTimeMillis();

            for (int i = 0; i <= toInsert[j]; i++) {
                H.insert(i);
            }
            for (int i = 1; i <= toDelete[j]; i++) {
                H.deleteMin();
            }
            long end = System.currentTimeMillis();

            System.out.println();
            System.out.println("Round : m = " + toInsert[j]);
            System.out.println("Run-Time : " + (end-start));
            System.out.println("totalLinks : " + FibonacciHeapQ2.totalLinks());
            System.out.println("totalCuts : " + FibonacciHeapQ2.totalCuts());
            System.out.println("Potential : " + H.potential());
        }
    }

    public static void fillChartTest() {
        FibonacciHeapQ2 H = new FibonacciHeapQ2();
        int[] toInsert = {8,80};
        int[] toDelete = {6,60};

        for (int j = 0; j < toInsert.length; j++) {
            long start = System.currentTimeMillis();

            for (int i = 0; i <= toInsert[j]; i++) {
                H.insert(i);
            }
            for (int i = 1; i <= toDelete[j]; i++) {
                H.deleteMin();
            }
            long end = System.currentTimeMillis();

            System.out.println();
            System.out.println("Round : m = " + toInsert[j]);
            System.out.println("Run-Time : " + (end-start));
            System.out.println("totalLinks : " + FibonacciHeapQ2.totalLinks());
            System.out.println("totalCuts : " + FibonacciHeapQ2.totalCuts());
            System.out.println("Potential : " + H.potential());
        }
    }

}
