package com.coldcore.ex.numtoword.impl;

import com.coldcore.ex.numtoword.PortionTuple;

import static com.coldcore.ex.numtoword.impl.PortionTupleImpl.negate;

/**
 * Tuple generator which also allows negative values
 */
public class NegativeCalculatorImpl extends PortionCalculatorImpl {

  @Override
  public PortionTuple asTuple(long n) {
    PortionTuple t = super.asTuple(Math.abs(n));
    if (n < 0) t = negate((PortionTupleImpl) t);
    return t;
  }

}
