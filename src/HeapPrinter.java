import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;


public class HeapPrinter {
    static final PrintStream stream = System.out;
    static void printIndentPrefix(ArrayList<Boolean> hasNexts) {
        int size = hasNexts.size();
        for (int i = 0; i < size - 1; ++i) {
            stream.format("%c   ", hasNexts.get(i).booleanValue() ? '│' : ' ');
        }
    }

    static void printIndent(FibonacciHeap.HeapNode heapNode, ArrayList<Boolean> hasNexts) {
        int size = hasNexts.size();
        printIndentPrefix(hasNexts);

        stream.format("%c── %s\n",
            hasNexts.get(size - 1) ? '├' : '╰',
            heapNode == null ? "(null)" : String.valueOf(heapNode.getKey())
        );
    }

    static String repeatString(String s,int count){
        StringBuilder r = new StringBuilder();
        for (int i = 0; i < count; i++) {
            r.append(s);
        }
        return r.toString();
    }

    static void printIndentVerbose(FibonacciHeap.HeapNode heapNode, ArrayList<Boolean> hasNexts) {
        int size = hasNexts.size();
        if (heapNode == null) {
            printIndentPrefix(hasNexts);
            stream.format("%c── %s\n", hasNexts.get(size - 1) ? '├' : '╰', "(null)");
            return;
        }

        Function<Supplier<FibonacciHeap.HeapNode>, String> keyify = (f) -> {
                FibonacciHeap.HeapNode node = f.get();
                return node == null ? "(null)" : String.valueOf(node.getKey());
        };
        String title  = String.format(" Key: %d ", heapNode.getKey());
        List<String> content =  Arrays.asList(
            String.format(" Rank: %d ", heapNode.getRank()),
            String.format(" Marked: %b ", heapNode.getMarked()),
            String.format(" Parent: %s ", keyify.apply(heapNode::getParent)),
            String.format(" Next: %s ", keyify.apply(heapNode::getNext)),
            String.format(" Prev: %s ", keyify.apply(heapNode::getPrev)),
            String.format(" Child: %s", keyify.apply(heapNode::getChild))
        );

        /* Print details in box */
        int length = Math.max(
            title.length(),
            content.stream().map(String::length).max(Integer::compareTo).get()
        );
        String line = repeatString("─", length);
        String padded = String.format("%%-%ds", length);
        boolean hasNext = hasNexts.get(size - 1);

        //print header row
        printIndentPrefix(hasNexts);
        stream.format("%c── ╭%s╮%n", hasNext ? '├' : '╰', line);

        //print title row
        printIndentPrefix(hasNexts);
        stream.format("%c   │" + padded + "│%n", hasNext ? '│' : ' ', title);

        // print separator
        printIndentPrefix(hasNexts);
        stream.format("%c   ├%s┤%n", hasNext ? '│' : ' ', line);

        // print content
        for (String data : content) {
            printIndentPrefix(hasNexts);
            stream.format("%c   │" + padded + "│%n", hasNext ? '│' : ' ', data);
        }

        // print footer
        printIndentPrefix(hasNexts);
        stream.format("%c   ╰%s╯%n", hasNext ? '│' : ' ', line);
    }

    static void printHeapNode(FibonacciHeap.HeapNode heapNode, FibonacciHeap.HeapNode until, ArrayList<Boolean> hasNexts, boolean verbose) {
        if (heapNode == null || heapNode == until) {
            return;
        }
        hasNexts.set(
            hasNexts.size() - 1,
            heapNode.getNext() != null && heapNode.getNext() != heapNode && heapNode.getNext() != until
        );
        if (verbose) {
            printIndentVerbose(heapNode, hasNexts);
        } else {
            printIndent(heapNode, hasNexts);
        }

        hasNexts.add(false);
        printHeapNode(heapNode.getChild(), null, hasNexts, verbose);
        hasNexts.remove(hasNexts.size() - 1);

        until = until == null ? heapNode : until;
        printHeapNode(heapNode.getNext(), until, hasNexts, verbose);
    }

    public static void print(FibonacciHeap heap, boolean verbose) {
        if (heap == null) {
            stream.println("(null)");
            return;
        } else if (heap.isEmpty()) {
            stream.println("(empty)");
            return;
        }

        stream.println("╮");
        ArrayList<Boolean> list = new ArrayList<>();
        list.add(false);
        printHeapNode(heap.getHead(), null, list, verbose);
    }

    public static void test1() {
        /* Build an example */
        FibonacciHeap fibHeap = new FibonacciHeap();

        stream.println("Printing in verbose mode:");
        HeapPrinter.print(fibHeap, true);

        Heap heap = new Heap();
        for (int i = 0; i < 10; i++) {//@@@@@@@ i<1000 @@@@@
            heap.insert(10 + i);
            fibHeap.insert(10 + i);
        }
        System.out.println("curr min 1 = " + fibHeap.findMin().getKey());
        for (int i = 9; i >= 0; i--) {
            heap.insert(30 + i);
            fibHeap.insert(30 + i);
        }
        //System.out.println("curr min 2 = " + fibHeap.findMin().getKey());
        //System.out.println("size= " + fibHeap.size());
        ArrayList<FibonacciHeap.HeapNode> nodes = new ArrayList<>();

        for (int i = 20; i < 30; i++) {
            heap.insert(i);
            nodes.add(fibHeap.insert(i));
        }
        //System.out.println("curr min 3 = " + fibHeap.findMin().getKey());

        //stream.println("Printing in verbose mode:");
        //HeapPrinter.print(fibHeap, true);

        for (int i = 20; i < 25; i++) {
            System.out.println();
            System.out.println("round " + i);
            if (heap.findMin() != fibHeap.findMin().getKey()) {
                System.out.println("error1");
            } else if (heap.size() != fibHeap.size()) {
                System.out.println("error2");
            }
            //System.out.println("curr min 4 = " + fibHeap.findMin().getKey());
            System.out.println("key to delete=" + nodes.get(i-20).getKey());
            heap.delete(i);
            fibHeap.delete(nodes.get(i - 20));

            stream.println("Printing in verbose mode:");
            HeapPrinter.print(fibHeap, true);

            System.out.println("potential=" + fibHeap.potential());
            System.out.println("tree count=" + fibHeap.getTreeCount());
            System.out.println("markedCount="+ fibHeap.getMarkedCount());
            System.out.println("total cuts=" + FibonacciHeap.totalCuts());
        }

        System.out.println();
        //System.out.println("deleteMin round:");

//        int i = 0;
//        while (!heap.isEmpty()) {
//            System.out.println();
//            System.out.println("round " + i++);
//            if (heap.findMin() != fibHeap.findMin().getKey() || heap.size() != fibHeap.size()) {
//                System.out.println("error3");
//            }
//            heap.deleteMin();
//            fibHeap.deleteMin();
            //System.out.println("curr min 5 = " + fibHeap.findMin().getKey());

//        }
//        if (!fibHeap.isEmpty()) {
//            System.out.println("error4");
//        }

//        stream.println("Printing in regular mode:");
//        HeapPrinter.print(heap, false);
    }

    public static void test2() {
        FibonacciHeap fibHeap = new FibonacciHeap();

        stream.println("Printing in verbose mode:");
        HeapPrinter.print(fibHeap, true);

        int cuts = FibonacciHeap.totalCuts();
        int links = FibonacciHeap.totalLinks();

        System.out.println("counterRep empty:");
        int[] counter1 = fibHeap.countersRep();
        for (int i = 0; i < counter1.length; i++) {
            System.out.println(counter1[i]);
        }

        fibHeap.insert(1);
        fibHeap.insert(2);
        fibHeap.insert(3);
        fibHeap.insert(4);
        fibHeap.insert(5);
        fibHeap.insert(6);
        fibHeap.insert(7);
        fibHeap.insert(8);
        fibHeap.insert(9);
        fibHeap.insert(10);
        fibHeap.insert(11);
        fibHeap.insert(12);
        fibHeap.insert(13);

        fibHeap.deleteMin();

        System.out.println();
        System.out.println("counterRep not empty:");
        int[] counter3 = fibHeap.countersRep();
        for (int i = 0; i < counter3.length; i++) {
            System.out.println(counter3[i]);
        }

        stream.println("Printing in verbose mode:");
        HeapPrinter.print(fibHeap, true);

//        if (fibHeap.potential() != 3) {
//            System.out.println("error1");
//        }
//        if (FibonacciHeap.totalCuts() - cuts != 0) {
//            System.out.println("error2");
//        }
//        if (FibonacciHeap.totalLinks() - links != 0) {
//            System.out.println("error3");
//        }
//        if (fibHeap.countersRep()[0] != 3) {
//            System.out.println("error4");
//        }

    }
    public static void test3() {
        FibonacciHeap fibHeap = new FibonacciHeap();
        for (int i = 0; i < 33; i++) {
            fibHeap.insert(i);
        }
        fibHeap.deleteMin();

        stream.println("Printing in verbose mode:");
        HeapPrinter.print(fibHeap, false);

        int[] kmin = FibonacciHeap.kMin(fibHeap, 10);
        for (int i = 0; i < kmin.length; i++) {
            if (kmin[i] != i + 1) {
                System.out.println("i=" + i);
            }
        }
    }

    public static void test4() {
        FibonacciHeap fibHeap = new FibonacciHeap();

        FibonacciHeap.HeapNode node0 = fibHeap.insert(0);
        FibonacciHeap.HeapNode node1 = fibHeap.insert(1);
        FibonacciHeap.HeapNode node2 = fibHeap.insert(2);
        FibonacciHeap.HeapNode node3 = fibHeap.insert(3);
        FibonacciHeap.HeapNode node4 = fibHeap.insert(4);
        FibonacciHeap.HeapNode node5 = fibHeap.insert(5);
        FibonacciHeap.HeapNode node6 = fibHeap.insert(6);
        FibonacciHeap.HeapNode node7 = fibHeap.insert(7);
        FibonacciHeap.HeapNode node8 = fibHeap.insert(8);
        FibonacciHeap.HeapNode node9 = fibHeap.insert(9);
        FibonacciHeap.HeapNode node10 = fibHeap.insert(10);
        FibonacciHeap.HeapNode node11 = fibHeap.insert(11);
        FibonacciHeap.HeapNode node12 = fibHeap.insert(12);
        FibonacciHeap.HeapNode node13 = fibHeap.insert(13);

        fibHeap.deleteMin();
        stream.println("Printing in verbose mode:");
        HeapPrinter.print(fibHeap, false);
        System.out.println("total cuts=" + FibonacciHeap.totalCuts());
        System.out.println("tree count=" + fibHeap.getTreeCount());
        System.out.println("marked count=" + fibHeap.getMarkedCount());

        fibHeap.decreaseKey(node11, 11);
        stream.println("Printing in verbose mode:");
        HeapPrinter.print(fibHeap, false);

        System.out.println("total cuts=" + FibonacciHeap.totalCuts());
        System.out.println("tree count=" + fibHeap.getTreeCount());
        System.out.println("marked count=" + fibHeap.getMarkedCount());

        fibHeap.decreaseKey(node13, 14);
        stream.println("Printing in verbose mode:");
        HeapPrinter.print(fibHeap, false);

        System.out.println("total cuts=" + FibonacciHeap.totalCuts());
        System.out.println("tree count=" + fibHeap.getTreeCount());
        System.out.println("marked count=" + fibHeap.getMarkedCount());

        fibHeap.decreaseKey(node12, 13);
        stream.println("Printing in verbose mode:");
        HeapPrinter.print(fibHeap, false);

        System.out.println("total cuts=" + FibonacciHeap.totalCuts());
        System.out.println("tree count=" + fibHeap.getTreeCount());
        System.out.println("marked count=" + fibHeap.getMarkedCount());

    }
    public static void test5() {
        FibonacciHeap fibHeap = new FibonacciHeap();

        int[] ms = new int[1];
        //int[] powers = {10, 15, 20, 25};
        int[] powers = {4};
        for (int i = 0; i < ms.length; i++) {
            ms[i] = (int) Math.pow(2, powers[i]);
        }

        for (int j = 0; j < ms.length; j++) {
            FibonacciHeap.HeapNode[] nodes = new FibonacciHeap.HeapNode[ms[j] + 1];

            int[] powerToM = new int[powers[j]];
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
                fibHeap.decreaseKey(nodes[(ms[j] - powerToM[i] + 2)], ms[j] + 1);
            }
            HeapPrinter.print(fibHeap, false);
            System.out.println(nodes[(ms[j] - 1)].getKey());
            fibHeap.decreaseKey(nodes[(ms[j] - 1)], ms[j] + 1);

            long end = System.currentTimeMillis();

            System.out.println();
            HeapPrinter.print(fibHeap, false);
            System.out.println("Round : m power = " + powers[j]);
            System.out.println("Run-Time : " + (end - start));
            System.out.println("total cuts= " + FibonacciHeap.totalCuts());
            System.out.println("total marked= " + fibHeap.getMarkedCount());
            System.out.println("tree count= " + fibHeap.getTreeCount());
            System.out.println("potential= " + fibHeap.potential());

        }
    }

        public static void test6 () {
            FibonacciHeap H = new FibonacciHeap();

            int[] toInsert = {8, 80};
            int[] toDelete = {6, 60};

            for (int j = 0; j < toInsert.length; j++) {
                long start = System.currentTimeMillis();

                for (int i = 0; i <= toInsert[j]; i++) {
                    H.insert(i);
                }

                for (int i = 1; i <= toDelete[j]; i++) {
                    H.deleteMin();
                    HeapPrinter.print(H, false);
                }
                long end = System.currentTimeMillis();

                System.out.println();
                System.out.println("Round : m = " + toInsert[j]);
                System.out.println("Run-Time : " + (end - start));
                System.out.println("totalLinks : " + FibonacciHeap.totalLinks());
                System.out.println("totalCuts : " + FibonacciHeap.totalCuts());
                System.out.println("Potential : " + H.potential());

                HeapPrinter.print(H, false);
            }
        }

        public static void test7 () {
            FibonacciHeap fibHeap = new FibonacciHeap();

            FibonacciHeap.HeapNode node5 = fibHeap.insert(5);
            FibonacciHeap.HeapNode node6 = fibHeap.insert(6);
            FibonacciHeap.HeapNode node7 = fibHeap.insert(7);
            FibonacciHeap.HeapNode node8 = fibHeap.insert(8);
            FibonacciHeap.HeapNode node0 = fibHeap.insert(0);
            FibonacciHeap.HeapNode node1 = fibHeap.insert(1);
            FibonacciHeap.HeapNode node2 = fibHeap.insert(2);
            FibonacciHeap.HeapNode node3 = fibHeap.insert(3);
            FibonacciHeap.HeapNode node4 = fibHeap.insert(4);
            FibonacciHeap.HeapNode node9 = fibHeap.insert(9);
            FibonacciHeap.HeapNode node10 = fibHeap.insert(10);
            FibonacciHeap.HeapNode node11 = fibHeap.insert(11);
            FibonacciHeap.HeapNode node12 = fibHeap.insert(12);
            FibonacciHeap.HeapNode node13 = fibHeap.insert(13);

            stream.println("Printing in verbose mode:");
            HeapPrinter.print(fibHeap, false);

            fibHeap.deleteMin();
            stream.println("Printing in verbose mode:");
            HeapPrinter.print(fibHeap, false);

            fibHeap.deleteMin();
            stream.println("Printing in verbose mode:");
            HeapPrinter.print(fibHeap, true);

//        System.out.println("total cuts=" + FibonacciHeap.totalCuts());
//        System.out.println("tree count=" + fibHeap.getTreeCount());
//        System.out.println("marked count=" + fibHeap.getMarkedCount());
//
//        fibHeap.decreaseKey(node11, 11);
//        stream.println("Printing in verbose mode:");
//        HeapPrinter.print(fibHeap, false);
//
//        System.out.println("total cuts=" + FibonacciHeap.totalCuts());
//        System.out.println("tree count=" + fibHeap.getTreeCount());
//        System.out.println("marked count=" + fibHeap.getMarkedCount());
//
//        fibHeap.decreaseKey(node13, 14);
//        stream.println("Printing in verbose mode:");
//        HeapPrinter.print(fibHeap, false);
        }

        public static void test8 () {
            FibonacciHeap H1 = new FibonacciHeap();
            FibonacciHeap H2 = new FibonacciHeap();

            H1.insert(13);
            H1.insert(10);
            H1.insert(5);
            H1.insert(20);
            H1.insert(17);

            H1.deleteMin();
            HeapPrinter.print(H1, false);


            H2.insert(4);
            H2.insert(1);
            H2.insert(3);
            H2.insert(0);
            H2.insert(2);

            //H2.deleteMin();
            HeapPrinter.print(H2, false);

            H1.meld(H2);
            HeapPrinter.print(H1, false);

            //H1.deleteMin();
            HeapPrinter.print(H1, false);

            System.out.println(H1.size());
            System.out.println(H1.getTreeCount());
            System.out.println(H1.getHead().getKey());
            System.out.println(H1.getTail().getKey());
            System.out.println(H1.findMin().getKey());


        }
        public static void Q2() {
            FibonacciHeap fibHeap = new FibonacciHeap();

            int power = 2;
            int m = (int) Math.pow(3, power)-1;
            int toDel = 3*m/4;


            long start = System.currentTimeMillis();

            for (int i = 0; i <= m; i++) {
                fibHeap.insert(i);
            }
            //HeapPrinter.print(fibHeap, false);

            for (int i = 1; i <= toDel; i++) {
                //System.out.println("curr min to delete: " + fibHeap.findMin().getKey());
                fibHeap.deleteMin();

            }
            long end = System.currentTimeMillis();

            HeapPrinter.print(fibHeap, false);

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



    public static void main(String[] args) {
        Q2();
    }

}