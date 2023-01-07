package com.coldcore.ex.numtoword;

/**
 * A container class (IMMUTABLE).
 * The original value can be calculated as: FIRST*pow(POWER)+SECOND
 */
public interface PortionTuple {

  /** @return Firts part of the number [0..999] */
  long getFirst();

  /** @return Second part of the number [0..MAX_VALUE]*/
  long getSecond();

  /** @return Power of the first part */
  int getPower();

  /** @return True if the tuple is positive */
  public boolean isPositive();

}
