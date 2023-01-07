package uk.gov.dwp.maze;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StringTest {

  private boolean verify(String s) {
    var countMap = s.chars().mapToObj(i -> (char)i).collect(groupingBy(c -> c, counting()));
    var max = countMap.values().stream().reduce((i1, i2) -> i1 < i2 ? i1 : i2).get();
    var countMap2 = new HashMap<Character,Long>();
    countMap.entrySet().forEach(me -> {
      countMap2.put(me.getKey(), Math.abs(me.getValue()-max));
      //System.out.println(me.getKey()+" -> "+Math.abs(me.getValue()-max));
    });

    var sorted = countMap2.values().stream().sorted().mapToLong(i -> i).toArray();
    var grouped = countMap2.values().stream().collect(groupingBy(i -> i, counting()));
    var match = grouped.size() == 1;
    if (!match && grouped.size() == 2) {
      var noHead = Arrays.copyOfRange(sorted, 1, sorted.length);
      var noTail = Arrays.copyOfRange(sorted, 0, sorted.length - 1);
      match |= (sorted[0] == 0) && Arrays.stream(noHead).mapToObj(i -> (long) i).collect(groupingBy(i -> i, counting())).size() == 1;
      match |= (sorted[sorted.length-1] == 1) && Arrays.stream(noTail).mapToObj(i -> (long) i).collect(groupingBy(i -> i, counting())).size() == 1;
    }
    return match;
  }

  @Test
  public void foo() throws Exception {
    assertTrue(verify("aabbccdd"));
    assertTrue(verify("aaabbccdd"));
    assertTrue(verify("abbccdd"));
    assertTrue(verify("abbbcccddd"));
    assertFalse(verify("abbbcccdddff"));
    assertFalse(verify("abbbcccdddf"));
    assertFalse(verify("aaaaaaaaaabbbcccddd"));
  }

}
