package fizzbuzz.step3;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Processor {

    private final static String LUCK = "luck";
    private final static String FIZZ = "fizz";
    private final static String BUZZ = "buzz";
    private final static String NUMBER = "number";

    private Object onNumber(int number) {
        return
            (""+number).contains("3") ? LUCK :
                number % 3 == 0 && number % 5 == 0 ? FIZZ+BUZZ :
                        number % 5 == 0 ? BUZZ :
                                number % 3 == 0 ? FIZZ :
                                        number;
    }

    public List<String> process(List<Integer> input) {
        return input.stream()
                .map(this::onNumber)
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    public OutputWithStats processWithStats(List<Integer> input) {
        List<Object> list =
                input.stream()
                .map(this::onNumber)
                .collect(Collectors.toList());

        Map<String, Integer> stats =
                list.stream()
                .collect(Collectors.toMap(
                        k -> (k instanceof String ? k : NUMBER).toString(),
                        v -> 1,
                        (v1, v2) -> v1+v2
                ));

        List<String> output =
                list.stream()
                .map(Object::toString)
                .collect(Collectors.toList());

        return new OutputWithStats(output, stats);
    }
}
