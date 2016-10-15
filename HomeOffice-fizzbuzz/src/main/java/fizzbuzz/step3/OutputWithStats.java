package fizzbuzz.step3;

import java.util.List;
import java.util.Map;

public class OutputWithStats {
    public final List<String> output;
    public final Map<String,Integer> stats;

    public OutputWithStats(List<String> output, Map<String, Integer> stats) {
        this.output = output;
        this.stats = stats;
    }
}
