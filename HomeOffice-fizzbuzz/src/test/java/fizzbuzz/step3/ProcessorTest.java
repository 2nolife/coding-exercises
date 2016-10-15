package fizzbuzz.step3;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ProcessorTest {

    private String[] expectedOutput = new String[]{
            "1", "2", "luck", "4", "buzz", "fizz", "7", "8", "fizz", "buzz", "11", "fizz", "luck", "14",
            "fizzbuzz", "16", "17", "fizz", "19", "buzz"
    };

    private List<Integer> input = IntStream.range(1, 21).mapToObj(x -> x).collect(Collectors.toList());

    @Test
    public void acceptance_test() {
        Processor processor = new Processor();
        List<String> output = processor.process(input);

        assertArrayEquals(expectedOutput, output.toArray());
    }

    @Test
    public void acceptance_test2() {
        Processor processor = new Processor();
        OutputWithStats outputWithStats = processor.processWithStats(input);

        Map<String,Integer> expectedStats = new HashMap<>();
        expectedStats.put("fizz", 4);
        expectedStats.put("buzz", 3);
        expectedStats.put("fizzbuzz", 1);
        expectedStats.put("luck", 2);
        expectedStats.put("number", 10);

        assertArrayEquals(expectedOutput, outputWithStats.output.toArray());
        assertEquals(expectedStats, outputWithStats.stats);

    }
}
