import java.util.ArrayList;
import java.util.Collections;

public class RunQ2 {

    public static void main(String[] arg) {
        Q2();
    }

    public static void Q2() {
        FibonacciHeap fibHeap = new FibonacciHeap();

        int power = 14;
        int m = (int) Math.pow(3, power)-1;
        int toDel = 3*m/4;

        long start = System.currentTimeMillis();

        for (int i = 0; i <= m; i++) {
            fibHeap.insert(i);
        }

        for (int i = 1; i <= toDel; i++) {
            fibHeap.deleteMin();
            if (i == 1) {
                System.out.println("tree count= " + fibHeap.getTreeCount());
            }

        }
        long end = System.currentTimeMillis();

        System.out.println();
        System.out.println("m = " + m);
        System.out.println("3m/4 = " + toDel);
        System.out.println("Run-Time : " + (end - start));
        System.out.println("total links= " + FibonacciHeap.totalLinks());
        System.out.println("total cuts= " + FibonacciHeap.totalCuts());
        System.out.println("total marked= " + fibHeap.getMarkedCount());
        System.out.println("tree count= " + fibHeap.getTreeCount());
        System.out.println("potential= " + fibHeap.potential());
    }


    public static void Q1() {
        FibonacciHeap fibHeap = new FibonacciHeap();

        //int[] powers = {10, 15, 20, 25};
        int power = 4;
        int m = (int) Math.pow(2, power);


        FibonacciHeap.HeapNode[] nodes = new FibonacciHeap.HeapNode[m + 1];

        int[] powerToM = new int[power];
        for (int i = 0; i < powerToM.length; i++) {
            powerToM[i] = (int) Math.pow(2, i+1);
        }

        long start = System.currentTimeMillis();

        for (int i = nodes.length - 1; i >= 0; i--) {
            FibonacciHeap.HeapNode curr = fibHeap.insert(i - 1);
            nodes[i] = curr;
        }

        fibHeap.deleteMin();

        for (int i = powerToM.length - 1; i >= 0; i--) {
            System.out.println(nodes[(m - powerToM[i] + 2)].getKey());
            fibHeap.decreaseKey(nodes[(m - powerToM[i] + 2)], m + 1);
        }
        long end = System.currentTimeMillis();

        System.out.println();
        System.out.println("Round : m power = " + power);
        System.out.println("Run-Time : " + (end - start));
        System.out.println("total cuts= " + FibonacciHeap.totalCuts());
        System.out.println("total links= " + FibonacciHeap.totalLinks());
        System.out.println("total marked= " + fibHeap.getMarkedCount());
        System.out.println("tree count= " + fibHeap.getTreeCount());
        System.out.println("potential= " + fibHeap.potential());


    }

}
