package com.coldcore.ex.numtoword;

import com.coldcore.ex.numtoword.impl.NegativeCalculatorImpl;
import com.coldcore.ex.numtoword.impl.PortionCalculatorImpl;
import com.coldcore.ex.numtoword.impl.PortionWriterImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RunnerTest {

  Runner runner;

  @Before
  public void pre() {
    runner = new Runner();
    runner.setCalculator(new PortionCalculatorImpl());
    runner.setWriter(new PortionWriterImpl());
  }

  @Test
  public void acceptanceCriteria() {
    assertEquals("nil", runner.numberToWord(0));
    assertEquals("one", runner.numberToWord(1));
    assertEquals("sixteen", runner.numberToWord(16));
    assertEquals("one hundred", runner.numberToWord(100));
    assertEquals("one hundred eighteen", runner.numberToWord(118));
    assertEquals("two hundred", runner.numberToWord(200));
    assertEquals("two hundred nineteen", runner.numberToWord(219));
    assertEquals("eight hundred", runner.numberToWord(800));
    assertEquals("eight hundred one", runner.numberToWord(801));
    assertEquals("one thousand three hundred sixteen", runner.numberToWord(1316));
    assertEquals("one million", runner.numberToWord(1000000));
    assertEquals("two millions", runner.numberToWord(2000000));
    assertEquals("three millions two hundred", runner.numberToWord(3000200));
    assertEquals("seven hundred thousand", runner.numberToWord(700000));
    assertEquals("nine millions", runner.numberToWord(9000000));
    assertEquals("nine millions one thousand", runner.numberToWord(9001000));
    assertEquals("one hundred twenty three millions four hundred fifty six thousand seven hundred eighty nine", runner.numberToWord(123456789));
    assertEquals("two billion one hundred forty seven millions four hundred eighty three thousand six hundred forty seven", runner.numberToWord(2147483647));
    assertEquals("three billion ten", runner.numberToWord(3000000010L));
    assertEquals("ninety nine billion", runner.numberToWord(99000000000L));
    assertEquals("eighty eight billion five", runner.numberToWord(88000000005L));
  }

  @Test
  public void negativeAcceptanceCriteria() {
    runner.setCalculator(new NegativeCalculatorImpl());
    assertEquals("nil", runner.numberToWord(0));
    assertEquals("minus one", runner.numberToWord(-1));
    assertEquals("minus one hundred eighteen", runner.numberToWord(-118));
    assertEquals("one thousand three hundred sixteen", runner.numberToWord(1316));
    assertEquals("minus nine millions one thousand", runner.numberToWord(-9001000));
    assertEquals("one hundred twenty three millions four hundred fifty six thousand seven hundred eighty nine", runner.numberToWord(123456789));
    assertEquals("minus two billion one hundred forty seven millions four hundred eighty three thousand six hundred forty seven", runner.numberToWord(-2147483647));
    assertEquals("ninety nine billion", runner.numberToWord(99000000000L));
    assertEquals("minus eighty eight billion five", runner.numberToWord(-88000000005L));
  }

  @Test
  public void chequeToWord() {
    assertEquals("one thousand three hundred sixteen pounds and thirteen pence only", runner.chequeToWord(1316, 13));
    assertEquals("seven hundred thousand pounds and fifty pence only", runner.chequeToWord(700000, 50));
  }

}
