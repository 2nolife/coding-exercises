package com.acme.serviceavailability;

public interface AvailabilityChecker {

  static final String SERVICE_AVAILABLE = "SERVICE_AVAILABLE";
  static final String SERVICE_UNAVAILABLE = "SERVICE_UNAVAILABLE";
  static final String SERVICE_PLANNED = "SERVICE_PLANNED";
  static final String POSTCODE_INVALID = "POSTCODE_INVALID"; 

  String isPostCodeIn3DTVServiceArea(String postCode) throws TechnicalFailureException;
}
