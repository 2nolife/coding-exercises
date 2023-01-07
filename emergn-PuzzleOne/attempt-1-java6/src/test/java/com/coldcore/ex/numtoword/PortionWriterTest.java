package com.coldcore.ex.numtoword;

import com.coldcore.ex.numtoword.impl.PortionCalculatorImpl;
import com.coldcore.ex.numtoword.impl.PortionWriterImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PortionWriterTest {

  PortionWriterImpl writer;
  PortionCalculatorImpl calc;

  @Before
  public void pre() {
    writer = new PortionWriterImpl();
    calc = new PortionCalculatorImpl();
  }

  @Test
  public void asString() {
    assertEquals("one", writer.asString(calc.asTuple(1)));
    assertEquals("five", writer.asString(calc.asTuple(5)));
    assertEquals("sixty", writer.asString(calc.asTuple(60)));
    assertEquals("sixty five", writer.asString(calc.asTuple(65)));
  }

  @Test
  public void asStringComplex() {
    assertEquals("one hundred eighteen", writer.asString(calc.asTuples(118)));
    assertEquals("eight hundred", writer.asString(calc.asTuples(800)));
    assertEquals("eight hundred one", writer.asString(calc.asTuples(801)));
    assertEquals("one thousand three hundred sixteen", writer.asString(calc.asTuples(1316)));
  }

}
