package com.coldcore.ex.numtoword.impl;

import com.coldcore.ex.numtoword.PortionCalculator;
import com.coldcore.ex.numtoword.PortionTuple;

import java.util.ArrayList;
import java.util.List;

public class PortionCalculatorImpl implements PortionCalculator {

  public static final long MIN = 0;
  public static final long MAX = 99000000000L;

  @Override
  public PortionTuple next(PortionTuple t) {
    PortionTuple nt;
    if (t.getFirst() == 0) nt = t;
    else nt = asTuple(t.getSecond());
    return nt;
  }

  @Override
  public List<PortionTuple> asTuples(long n) {
    List<PortionTuple> l = new ArrayList<PortionTuple>();
    PortionTuple t;
    l.add(t = asTuple(n));
    while ((t = next(t)) != null) { //always true, saves extra code line "t = next(t)"
      if (t.getFirst() > 0) l.add(t);
      else break;
    }
    return  l;
  }

  @Override
  public PortionTuple asTuple(long n) {
    if (n < MIN || n > MAX)
      throw new IllegalArgumentException("Input parameter "+n+" outside of ["+MIN+" ... n ... "+MAX+"]");
    PortionTuple t;
    if (n < 100) t = new PortionTupleImpl(n, 0, 1);
    else {
      int len = (""+n).length();
      int p = len > 3 ? 0 : 2;
      while ((len -= 3) > 0) p += 3;
      long d = (long) Math.pow(10, p);
      t = new PortionTupleImpl(n/d, n%d, p);
    }
    return t;
  }

}
