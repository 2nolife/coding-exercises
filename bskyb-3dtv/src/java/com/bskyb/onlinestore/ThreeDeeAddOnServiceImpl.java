package com.bskyb.onlinestore;

import com.acme.serviceavailability.AvailabilityChecker;
import com.acme.serviceavailability.TechnicalFailureException;

import java.util.*;

public class ThreeDeeAddOnServiceImpl implements ThreeDeeAddOnService {

  /* Those products should come from some data source, hardcoded for simplcity */
  public static final String[] ALL_PRODUCTS = new String[]{"SPORTS", "KIDS", "VARIETY", "NEWS", "MOVIES_1", "MOVIES_2"};
  public static final String[] COMPATIBLE_PRODUCTS = new String[]{"NEWS", "SPORTS", "MOVIES_1", "MOVIES_2"};
  public static final String[] NOT_COMPATIBLE_PRODUCTS = new String[]{"KIDS", "VARIETY"};
  public static final String[] VALID_ADDONS = new String[]{"SPORTS_3D_ADD_ON", "NEWS_3D_ADD_ON", "MOVIES_3D_ADD_ON"};

  private static final List<String> compatible = Arrays.asList(COMPATIBLE_PRODUCTS);
  private static final List<String> addons = Arrays.asList(VALID_ADDONS);
  private static final String SUFFIX = "_3D_ADD_ON";

  private AvailabilityChecker availabilityChecker;


  public void setAvailabilityChecker(AvailabilityChecker availabilityChecker) {
    this.availabilityChecker = availabilityChecker;
  }


  private String selectAddonForProduct(String product) {
    String addon = null;
    if (compatible.contains(product)) {
      int i = product.lastIndexOf("_");
      if (i > 0) product = product.substring(0, i);
      addon = product+SUFFIX;
    }
    return addons.contains(addon) ? addon : null;
  }


  public Basket checkFor3DAddOnProducts(Basket basket, String postCode) {
    Basket result = new Basket();

    String response = null;
    try {
      response = availabilityChecker.isPostCodeIn3DTVServiceArea(postCode);
    } catch (TechnicalFailureException e) {
      //log the error
    }

    if (AvailabilityChecker.SERVICE_AVAILABLE.equals(response) && basket != null && basket.getItems() != null) {
      List<String> addons = new ArrayList<String>();
      for (String item : basket.getItems()) {
        String addon = selectAddonForProduct(item);
        if (addon != null) addons.add(addon);
      }
      result.setItems(addons.toArray(new String[addons.size()]));
    } 

    return result;
  }

}
