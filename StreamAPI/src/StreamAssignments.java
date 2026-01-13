import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StreamAssignments {

    // 1. Метод, возвращающий среднее значение списка целых чисел
    public static double getAverage(List<Integer> numbers) {
        return numbers.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }

    // 2. Метод, приводящий строки в верхний регистр и добавляющий префикс «_new_»
    public static List<String> modifyStrings(List<String> strings) {
        return strings.stream()
                .map(s -> "_new_" + s.toUpperCase())
                .collect(Collectors.toList());
    }

    // 3. Метод, возвращающий список квадратов элементов, встречающихся только один раз
    public static List<Integer> getSquaresOfUniqueElements(List<Integer> numbers) {
        return numbers.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() == 1)
                .map(entry -> entry.getKey() * entry.getKey())
                .collect(Collectors.toList());
    }

    // 4. Метод, возвращающий отсортированные строки, начинающиеся с заданной буквы
    public static List<String> filterAndSortStrings(Collection<String> strings, char startLetter) {
        String prefix = String.valueOf(startLetter);
        return strings.stream()
                .filter(s -> s.startsWith(prefix))
                .sorted()
                .collect(Collectors.toList());
    }

    // 5. Метод, возвращающий последний элемент коллекции или кидающий исключение
    public static <T> T getLastElement(Collection<T> collection) {
        return collection.stream()
                .reduce((first, second) -> second)
                .orElseThrow(() -> new NoSuchElementException("Коллекция пуста"));
    }

    // 6. Метод, возвращающий сумму чётных чисел в массиве
    public static int sumOfEvens(int[] numbers) {
        return Arrays.stream(numbers)
                .filter(n -> n % 2 == 0)
                .sum();
    }

    // 7. Метод, преобразовывающий строки в Map (первый символ - ключ, остальное - значение)
    public static Map<Character, String> convertToMap(List<String> strings) {
        return strings.stream()
                .filter(s -> s != null && !s.isEmpty())
                .collect(Collectors.toMap(
                        s -> s.charAt(0),
                        s -> s.substring(1),
                        (existing, replacement) -> replacement
                ));
    }
}
