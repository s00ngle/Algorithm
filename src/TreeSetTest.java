import java.util.TreeSet;

public class TreeSetTest {
    public static void main(String[] args) {
        TreeSet<Integer> set = new TreeSet<>();
        set.add(5);
        set.add(2);
        set.add(4);
        set.add(1);
        set.add(3);

        System.out.println(set.pollFirst());
        System.out.println(set.pollLast());
    }
}
