package TreeMap;

import java.util.TreeMap;

public class TreeMapTest {
    public static void main(String[] args) {
        TreeMap<Integer, String> map = new TreeMap<>();

        map.put(1, "apple");
        map.put(2, "banana");
        map.put(3, "coconut");

        System.out.println(map.pollFirstEntry());
        System.out.println(map.pollLastEntry());
    }
}
