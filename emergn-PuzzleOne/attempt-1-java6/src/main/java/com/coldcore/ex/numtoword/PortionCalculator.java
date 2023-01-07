package com.coldcore.ex.numtoword;

import java.util.List;

/**
 * Tuples generator.
 */
public interface PortionCalculator {

  /** @return Tuple with its second part shifted into the first part */
  PortionTuple next(PortionTuple t);

  /** @return List of tuples by sequentally applying NEXT until the number is exchausted */
  List<PortionTuple> asTuples(long n);

  /** @return Tuple containing the number */
  PortionTuple asTuple(long n);

}
