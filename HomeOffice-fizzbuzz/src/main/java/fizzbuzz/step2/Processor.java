package fizzbuzz.step2;

import java.util.List;
import java.util.stream.Collectors;

public class Processor {

    private final static String LUCK = "luck";
    private final static String FIZZ = "fizz";
    private final static String BUZZ = "buzz";

    private String onNumber(int number) {
        return
            (""+number).contains("3") ? LUCK :
                number % 3 == 0 && number % 5 == 0 ? FIZZ+BUZZ :
                        number % 5 == 0 ? BUZZ :
                                number % 3 == 0 ? FIZZ :
                                        ""+number;
    }

    public List<String> process(List<Integer> input) {
        return input.stream()
                .map(this::onNumber)
                .collect(Collectors.toList());
    }
}
