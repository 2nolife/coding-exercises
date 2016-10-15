package fizzbuzz.step1;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertArrayEquals;

public class ProcessorTest {

    @Test
    public void acceptance_test() {
        Processor processor = new Processor();
        List<Integer> input = IntStream.range(1, 21).mapToObj(x -> x).collect(Collectors.toList());
        List<String> output = processor.process(input);

        String[] expected = new String[]{
                "1", "2", "fizz", "4", "buzz", "fizz", "7", "8", "fizz", "buzz", "11", "fizz", "13", "14",
                "fizzbuzz", "16", "17", "fizz", "19", "buzz"
        };

        assertArrayEquals(expected, output.toArray());

    }
}
