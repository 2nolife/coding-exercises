package com.coldcore.ex.numtoword.impl;

import com.coldcore.ex.numtoword.PortionTuple;
import com.coldcore.ex.numtoword.PortionWriter;

import java.util.Collection;

public class PortionWriterImpl implements PortionWriter {

  private static final String[] nilTo19 = {
          "nil",
          "one",
          "two",
          "three",
          "four",
          "five",
          "six",
          "seven",
          "eight",
          "nine",
          "ten",
          "eleven",
          "twelve",
          "thirteen",
          "fourteen",
          "fifteen",
          "sixteen",
          "seventeen",
          "eighteen",
          "nineteen"
  };

  private static final String[] tens = {
           "ten",
           "twenty",
           "thirty",
           "forty",
           "fifty",
           "sixty",
           "seventy",
           "eighty",
           "ninety"
   };

  private static final String[] powers = {
           "hundred", "thousand", null, null, "million", "millions", null, "billion"
   };

  @Override
  public String asString(PortionTuple t) {
    String s = "";
    int f = (int) t.getFirst(), p = t.getPower();
    if (f < 20) s += nilTo19[f];
    else {
      if (f >= 100) {
        s += nilTo19[f/100]+" "+powers[0];
        f %= 100;
      }
      if (f > 0) s += (s.isEmpty() ? "" : " ")+tens[f/10-1]+(f%10 == 0 ? "" : " "+nilTo19[f%10]);
    }
    if (p > 1) s += " "+powers[p-(p == 6 && f > 1 ? 1 : 2)];
    return s;
  }

  @Override
  public String asString(Collection<PortionTuple> ts) {
    StringBuilder sb = new StringBuilder();
    for (PortionTuple t : ts)
      sb.append(" ").append(asString(t));
    boolean positive = ts.size() == 0 || ts.iterator().next().isPositive();
    return (positive ? "" : "minus ")+sb.delete(0,1).toString();
  }

}
