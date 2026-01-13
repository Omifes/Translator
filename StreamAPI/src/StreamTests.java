import java.util.*;

public class StreamTests {
    public static void main(String[] args) {
        System.out.println("=== STREAM API ТЕСТЫ ===\n");

        test1();
        test2();
        test3();
        test4();
        test5();
        test6();
        test7();
    }

    static void test1() {
        List<Integer> input = Arrays.asList(1, 2, 3, 4, 10);
        double output = StreamAssignments.getAverage(input);
        System.out.printf("1. Среднее: INPUT=[%s]%n   OUTPUT=%.1f%n   EXPECTED=4.0%n%n",
                input, output);
    }

    static void test2() {
        List<String> input = Arrays.asList("apple", "banana", "test");
        List<String> output = StreamAssignments.modifyStrings(input);
        System.out.printf("2. Префикс: INPUT=[%s]%n   OUTPUT=%s%n   EXPECTED=[_new_APPLE, _new_BANANA, _new_TEST]%n%n",
                input, output);
    }

    static void test3() {
        List<Integer> input = Arrays.asList(2, 3, 2, 4, 5, 5, 1);
        List<Integer> output = StreamAssignments.getSquaresOfUniqueElements(input);
        System.out.printf("3. Квадраты уникальных: INPUT=[%s]%n   OUTPUT=%s%n   EXPECTED=[1, 9, 16]%n%n",
                input, output);
    }

    static void test4() {
        List<String> input = Arrays.asList("apple", "apricot", "banana", "avocado", "cherry");
        List<String> output = StreamAssignments.filterAndSortStrings(input, 'a');
        System.out.printf("4. Фильтр 'a': INPUT=[%s]%n   OUTPUT=%s%n   EXPECTED=[apple, apricot, avocado]%n%n",
                input, output);
    }

    static void test5() {
        List<String> input = Arrays.asList("first", "second", "LAST");
        String output = StreamAssignments.getLastElement(input);
        System.out.printf("5. Последний: INPUT=[%s]%n   OUTPUT=%s%n   EXPECTED=LAST%n%n",
                input, output);
    }

    static void test6() {
        int[] input = {1, 2, 3, 4, 5, 6};
        int output = StreamAssignments.sumOfEvens(input);
        System.out.printf("6. Четные: INPUT=[%s]%n   OUTPUT=%d%n   EXPECTED=12%n%n",
                Arrays.toString(input), output);
    }

    static void test7() {
        List<String> input = Arrays.asList("apple", "banana", "cherry", "apricot");
        Map<Character, String> output = StreamAssignments.convertToMap(input);
        System.out.printf("7. Map: INPUT=[%s]%n   OUTPUT=%s%n   EXPECTED={a=pricot, b=anana, c=herry}%n%n",
                input, output);
    }
}
