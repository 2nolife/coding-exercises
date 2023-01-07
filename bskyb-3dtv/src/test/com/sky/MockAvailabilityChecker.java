package com.sky;

import com.acme.serviceavailability.AvailabilityChecker;
import com.acme.serviceavailability.TechnicalFailureException;

public class MockAvailabilityChecker implements AvailabilityChecker {

  public static final String MOCK_POSTCODE_AVAILABLE = "HA2 0PA";
  public static final String MOCK_POSTCODE_UNAVAILABLE = "HA2 0PU";
  public static final String MOCK_POSTCODE_PLANNED = "HA2 0PP";
  public static final String MOCK_POSTCODE_INVALID = "HA2 0PI";
  public static final String MOCK_POSTCODE_EXCEPTION = "EC3 UNK";

  public String isPostCodeIn3DTVServiceArea(String postCode) throws TechnicalFailureException {
    if (postCode == null) throw new TechnicalFailureException();
    if (postCode.equals(MOCK_POSTCODE_AVAILABLE)) return AvailabilityChecker.SERVICE_AVAILABLE;
    if (postCode.equals(MOCK_POSTCODE_UNAVAILABLE)) return AvailabilityChecker.SERVICE_UNAVAILABLE;
    if (postCode.equals(MOCK_POSTCODE_PLANNED)) return AvailabilityChecker.SERVICE_PLANNED;
    if (postCode.equals(MOCK_POSTCODE_INVALID)) return AvailabilityChecker.POSTCODE_INVALID;
    throw new TechnicalFailureException();
  }

}
