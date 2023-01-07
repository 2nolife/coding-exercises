package com.coldcore.ex.numtoword;

import com.coldcore.ex.numtoword.impl.PortionCalculatorImpl;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PortionCalculatorTest {

  PortionCalculatorImpl calc;

  @Before
  public void pre() {
    calc = new PortionCalculatorImpl();
  }

  /** Method to compare tuple with parameters */
  private void compareTuple(PortionTuple t, long f, long s, int p) {
    assertNotNull("Tuple is NULL", t);
    assertEquals("First portion of the tuple is wrong", f, t.getFirst());
    assertEquals("Second portion of the tuple is wrong", s, t.getSecond());
    assertEquals("Power portion of the tuple is wrong", p, t.getPower());
  }

  @Test
  public void asTupleInvalid() {
    calc.asTuple(PortionCalculatorImpl.MIN);
    try {
      calc.asTuple(PortionCalculatorImpl.MIN-1);
      fail("Invalid parameter accepted");
    } catch (IllegalArgumentException e) {}
    try {
      calc.asTuple(PortionCalculatorImpl.MAX+1);
      fail("Invalid parameter accepted");
    } catch (IllegalArgumentException e) {}
  }

  @Test
  public void asTuple() {
    compareTuple(calc.asTuple(0), 0, 0, 1);
    compareTuple(calc.asTuple(3), 3, 0, 1);
    compareTuple(calc.asTuple(45), 45, 0, 1);
    compareTuple(calc.asTuple(100), 1, 0, 2);
    compareTuple(calc.asTuple(168), 1, 68, 2);
    compareTuple(calc.asTuple(3246), 3, 246, 3);
    compareTuple(calc.asTuple(1000), 1, 0, 3);
    compareTuple(calc.asTuple(1200), 1, 200, 3);
    compareTuple(calc.asTuple(4067), 4, 67, 3);
    compareTuple(calc.asTuple(24067), 24, 67, 3);
    compareTuple(calc.asTuple(231067), 231, 67, 3);
    compareTuple(calc.asTuple(6231067), 6, 231067, 6);
    compareTuple(calc.asTuple(567231067), 567, 231067, 6);
    compareTuple(calc.asTuple(567031067), 567, 31067, 6);
    compareTuple(calc.asTuple(99000000000L), 99, 0, 9);
    compareTuple(calc.asTuple(88000000005L), 88, 5, 9);
  }

  @Test
  public void next() {
    PortionTuple t;
    t = calc.next(calc.asTuple(0));
    compareTuple(t, 0, 0, 1);
    t = calc.next(calc.asTuple(168));
    compareTuple(t, 68, 0, 1);
    t = calc.next(calc.asTuple(231067));
    compareTuple(t, 67, 0, 1);
    t = calc.next(calc.asTuple(567231067));
    compareTuple(t, 231, 67, 3);
    t = calc.next(calc.asTuple(99000000000L));
    compareTuple(t, 0, 0, 1);
    t = calc.next(calc.asTuple(88000000005L));
    compareTuple(t, 5, 0, 1);

    t = calc.next(calc.next(calc.asTuple(567231067)));
    compareTuple(t, 67, 0, 1);
    t = calc.next(t);
    compareTuple(t, 0, 0, 1);
  }

}
