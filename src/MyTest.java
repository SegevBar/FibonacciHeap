import java.util.ArrayList;
import java.util.Collections;

public class MyTest {
    public static void main(String[] arg) {

    }

    public static void test1() {
        FibonacciHeap H = new FibonacciHeap();

        ArrayList<Integer> numbers = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);

        for (int i = 0; i < 5; i++) {
            H.insert(numbers.get(i));
        }

        for (int i = 0; i < 5; i++) {
            if (H.findMin().getKey() != i) {

            }
            H.deleteMin();
        }
    }
}
