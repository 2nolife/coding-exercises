package com.coldcore.ex.numtoword.impl;

import com.coldcore.ex.numtoword.PortionTuple;

public class PortionTupleImpl implements PortionTuple {

  private long first;
  private long second;
  private int power;
  private boolean positive = true;

  public PortionTupleImpl(long first, long second, int power) {
    this.first = first;
    this.second = second;
    this.power = power;
  }

  /** @return Negative version of the tuple, since the class is immutable it does not have setters. */
  public static PortionTupleImpl negate(PortionTupleImpl t) {
    PortionTupleImpl nt = new PortionTupleImpl(t.first, t.second, t.power);
    nt.positive = false;
    return nt;
  }

  @Override
  public long getFirst() {
    return first;
  }

  @Override
  public long getSecond() {
    return second;
  }

  @Override
  public int getPower() {
    return power;
  }

  public boolean isPositive() {
    return positive;
  }

  @Override
  public String toString() {
    return "Tuple ["+first+"*pow("+power+")+"+second+"]";
  }
}
