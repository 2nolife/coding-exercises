package com.coldcore.ex.numtoword;

/**
 * A dummy class with getters and setters.
 */
public class Runner {

  private PortionCalculator calculator;
  private PortionWriter writer;

  public PortionCalculator getCalculator() {
    return calculator;
  }

  public void setCalculator(PortionCalculator calculator) {
    this.calculator = calculator;
  }

  public PortionWriter getWriter() {
    return writer;
  }

  public void setWriter(PortionWriter writer) {
    this.writer = writer;
  }

  private void assertConfigured() {
    if (calculator == null || writer == null)
      throw new IllegalStateException("Runner is not propery configured");
  }

  /** @return Number converted into words */
  public String numberToWord(long n) {
    assertConfigured();
    return writer.asString(calculator.asTuples(n));
  }

  /** @return Cheque amount converted into words */
  public String chequeToWord(long pounds, long pence) {
    assertConfigured();
    return writer.asString(calculator.asTuples(pounds))+" pounds and "+
            writer.asString(calculator.asTuples(pence))+" pence only";
  }
}
