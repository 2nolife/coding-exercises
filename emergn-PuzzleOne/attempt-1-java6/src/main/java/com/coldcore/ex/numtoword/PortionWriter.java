package com.coldcore.ex.numtoword;

import java.util.Collection;

/**
 * Converts tuples into words.
 */
public interface PortionWriter {

  /** @return Words describing the first part of the tuple and its power */
  String asString(PortionTuple t);

  /** @return Sentence describing the first part of all the tuples and their powers */
  String asString(Collection<PortionTuple> ts);

}
