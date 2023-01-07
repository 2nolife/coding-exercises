package com.sky;

import com.acme.serviceavailability.AvailabilityChecker;
import com.acme.serviceavailability.TechnicalFailureException;
import com.bskyb.onlinestore.Basket;
import com.bskyb.onlinestore.ThreeDeeAddOnService;
import com.bskyb.onlinestore.ThreeDeeAddOnServiceImpl;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

public class ThreeDeeAddOnServiceImplTest extends TestCase {

  private AvailabilityChecker availabilityChecker;
  private ThreeDeeAddOnServiceImpl service;
  private List<String> validAddons;

  protected void setUp() throws Exception {
    super.setUp();
    availabilityChecker = new MockAvailabilityChecker();
    service = new ThreeDeeAddOnServiceImpl();
    service.setAvailabilityChecker(availabilityChecker);
    validAddons = Arrays.asList(ThreeDeeAddOnServiceImpl.VALID_ADDONS);
  }


  private void ensureTechnicalFailureException(String postcode) {
    try {
      availabilityChecker.isPostCodeIn3DTVServiceArea(postcode);
      fail("No TechnicalFailureException on postcode "+postcode);
    } catch (AssertionFailedError e) {
      throw e;
    } catch (TechnicalFailureException e) {
      //pass
    } catch (Throwable e) {
      fail("Postcode "+postcode+". Caught "+e+", expected TechnicalFailureException");
    }
  }


  public void testAvailabilityMock() throws Throwable {
    ensureTechnicalFailureException(null);
    ensureTechnicalFailureException(MockAvailabilityChecker.MOCK_POSTCODE_EXCEPTION);
    assertEquals(MockAvailabilityChecker.MOCK_POSTCODE_AVAILABLE+" should be available", AvailabilityChecker.SERVICE_AVAILABLE,
            availabilityChecker.isPostCodeIn3DTVServiceArea(MockAvailabilityChecker.MOCK_POSTCODE_AVAILABLE));
    assertEquals(MockAvailabilityChecker.MOCK_POSTCODE_UNAVAILABLE+" should be unavailable", AvailabilityChecker.SERVICE_UNAVAILABLE,
            availabilityChecker.isPostCodeIn3DTVServiceArea(MockAvailabilityChecker.MOCK_POSTCODE_UNAVAILABLE));
    assertEquals(MockAvailabilityChecker.MOCK_POSTCODE_PLANNED+" should be planned", AvailabilityChecker.SERVICE_PLANNED,
            availabilityChecker.isPostCodeIn3DTVServiceArea(MockAvailabilityChecker.MOCK_POSTCODE_PLANNED));
    assertEquals(MockAvailabilityChecker.MOCK_POSTCODE_INVALID+" should be invalid", AvailabilityChecker.POSTCODE_INVALID,
            availabilityChecker.isPostCodeIn3DTVServiceArea(MockAvailabilityChecker.MOCK_POSTCODE_INVALID));
  }


  private boolean basketContains3DAddons(Basket basket) {
    if (basket != null && basket.getItems() != null)
      for (String item : basket.getItems())
        if (validAddons.contains(item)) return true;
    return false;
  }

  /*
    Acceptance criteria tests.
    Written in TDD mode bit by bit implementing the service when the test fails.
    */

  public void testNoInput() throws Throwable {
    assertNotNull("No input must return empty basket", service.checkFor3DAddOnProducts(null, null));
  }


  /*Acceptance criteria 1*/
  public void testServiceAvailableCompatible() throws Throwable {
    Basket basket = new Basket();
    basket.setItems(ThreeDeeAddOnServiceImpl.COMPATIBLE_PRODUCTS);
    assertTrue("No addons for compatible products and postcode (ONLY COMPATIBLE PRODUCTS)",
            basketContains3DAddons(service.checkFor3DAddOnProducts(basket, MockAvailabilityChecker.MOCK_POSTCODE_AVAILABLE)));

    basket.setItems(ThreeDeeAddOnServiceImpl.ALL_PRODUCTS);
    assertTrue("No addons for compatible products and postcode (ALL PRODUCTS)",
            basketContains3DAddons(service.checkFor3DAddOnProducts(basket, MockAvailabilityChecker.MOCK_POSTCODE_AVAILABLE)));

    for (String item : ThreeDeeAddOnServiceImpl.COMPATIBLE_PRODUCTS) {
      basket.setItems(new String[]{item});
      assertTrue("No addons for compatible product and postcode ("+item+")",
              basketContains3DAddons(service.checkFor3DAddOnProducts(basket, MockAvailabilityChecker.MOCK_POSTCODE_AVAILABLE)));
    }
  }

  /*Acceptance criteria 2*/
  public void testServiceNotAvailableAndNoCompatible() throws Throwable {
    Basket basket = new Basket();
    basket.setItems(ThreeDeeAddOnServiceImpl.NOT_COMPATIBLE_PRODUCTS);
    assertFalse("Addons detected for non compatible products with not available postcode",
            basketContains3DAddons(service.checkFor3DAddOnProducts(basket, null)));
    basket.setItems(ThreeDeeAddOnServiceImpl.ALL_PRODUCTS);
    assertFalse("Addons detected for compatible products with not available postcode",
            basketContains3DAddons(service.checkFor3DAddOnProducts(basket, null)));
  }

  /*Acceptance criteria 3*/
  public void testServicePlannedOrTechnicalAndCompatible() throws Throwable {
    Basket basket = new Basket();
    basket.setItems(ThreeDeeAddOnServiceImpl.ALL_PRODUCTS);
    assertFalse("Addons detected for compatible products with planned postcode",
            basketContains3DAddons(service.checkFor3DAddOnProducts(basket, MockAvailabilityChecker.MOCK_POSTCODE_PLANNED)));
    assertFalse("Addons detected for compatible products with exceptional postcode",
            basketContains3DAddons(service.checkFor3DAddOnProducts(basket, MockAvailabilityChecker.MOCK_POSTCODE_EXCEPTION)));
    assertFalse("Addons detected for compatible products with unavailable postcode",
            basketContains3DAddons(service.checkFor3DAddOnProducts(basket, MockAvailabilityChecker.MOCK_POSTCODE_UNAVAILABLE)));
  }

  /*Acceptance criteria 4*/
  public void testServiceInvalidAndCompatible() throws Throwable {
    Basket basket = new Basket();
    basket.setItems(ThreeDeeAddOnServiceImpl.ALL_PRODUCTS);
    assertFalse("Addons detected for compatible products with planned postcode",
            basketContains3DAddons(service.checkFor3DAddOnProducts(basket, MockAvailabilityChecker.MOCK_POSTCODE_INVALID)));
  }

}
