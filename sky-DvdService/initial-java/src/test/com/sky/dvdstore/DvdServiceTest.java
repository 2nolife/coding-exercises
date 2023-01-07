package com.sky.dvdstore;

import com.sky.dvdstore.impl.DvdServiceImpl;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

public class DvdServiceTest extends TestCase {

  private DvdService dvdService;


  protected void setUp() throws Exception {
    super.setUp();
    dvdService = new DvdServiceImpl(new DvdRepositoryStub());
  }


  /** Ensure that proper exception is thrown. */
  private void ensureDvdNotFoundException(String dvdReference, boolean callRetrieve) {
    try {
      if (callRetrieve) dvdService.retrieveDvd(dvdReference);
      else dvdService.getDvdSummary(dvdReference);
      fail("No DvdNotFoundException on ref "+dvdReference);
    } catch (AssertionFailedError e) {
      throw e;
    } catch (InvalidDvdReferenceException e) {
      fail("Ref "+dvdReference+". Caught InvalidDvdReferenceException, expected DvdNotFoundException");
    } catch (DvdNotFoundException e) {
      //pass
    } catch (Throwable e) {
      fail("Ref "+dvdReference+". Caught "+e+", expected DvdNotFoundException");
    }
  }


  /** Ensure that proper exception is thrown. */
  private void ensureInvalidDvdReferenceException(String dvdReference, boolean callRetrieve) {
    try {
      if (callRetrieve) dvdService.retrieveDvd(dvdReference);
      else dvdService.getDvdSummary(dvdReference);
      fail("No InvalidDvdReferenceException on ref "+dvdReference);
    } catch (AssertionFailedError e) {
      throw e;
    } catch (InvalidDvdReferenceException e) {
      //pass
    } catch (Throwable e) {
      fail("Ref "+dvdReference+". Caught "+e+", expected InvalidDvdReferenceException");
    }
  }

  /***
      Acceptance criteria tests, written in TDD mode.
      Follow the comments above tests to retrieve TDD steps.
   ***/

  /** TDD Note:
   * 1) written before any code added into "DvdServiceImpl.retrieveDvd", method returns NULL. RED
   * 2) added ref check to "DvdServiceImpl.retrieveDvd" (top line), method returns NULL. RED
   * 3) added code to get DVD to "DvdServiceImpl.retrieveDvd", method returns NULL. GREEN
   */
  public void testDvdNotFoundOnRetrieve() throws Throwable {
    ensureInvalidDvdReferenceException(null, true);
    ensureInvalidDvdReferenceException("", true);
    ensureInvalidDvdReferenceException("INVALID-TEXT", true);
    ensureDvdNotFoundException("DVD-999", true);
    /** TDD Note: added after step (3). RED. Method returns "dvd". GREEN */
    assertNotNull("Got NULL for ref DVD-TG423", dvdService.retrieveDvd("DVD-TG423"));
  }


  /** TDD Note:
   * 1) written after "testDvdNotFoundOnRetrieve" turned GREEN
   */
  public void testRetrieveDvd() throws Throwable {
    String ref = "DVD-TG423";
    Dvd dvd = dvdService.retrieveDvd(ref);
    assertNotNull("Got NULL for ref "+ref, dvd);
    assertEquals(ref+" ref mismatch", ref, dvd.getReference());
    assertEquals(ref+" title mismatch", "Topgun", dvd.getTitle());
  }


  /** TDD Note:
   * 1) written after above turned GREEN, but before any code added into "DvdServiceImpl.getDvdSummary", method returns NULL. RED
   * 2) added "retrieveDvd" call to "DvdServiceImpl.getDvdSummary", method returns NULL. GREEN
   */
  public void testDvdNotFoundOnSummary() throws Throwable {
    ensureInvalidDvdReferenceException(null, false);
    ensureInvalidDvdReferenceException("", false);
    ensureInvalidDvdReferenceException("INVALID-TEXT", false);
    ensureDvdNotFoundException("DVD-999", false);
    /** TDD Note: added after step (2). RED. Method returns string. GREEN */
    assertNotNull("Got NULL for ref DVD-TG423", dvdService.getDvdSummary("DVD-TG423"));
  }


  /** Note:
   * 1) written after "testDvdNotFoundOnSummary" turned GREEN. RED
   * 2) added code to "DvdServiceImpl.getDvdSummary" GREEN
   */
  public void testSummaryDvd() throws Throwable {
    String ref = "DVD-TG423";
    String summary = dvdService.getDvdSummary(ref);
    assertNotNull("Got NULL for ref "+ref, summary);
    assertEquals(ref+" summary mismatch", ref+" Topgun - All action film", summary);
    /** TDD Note: added after step (2). RED. Updated summary code. GREEN */
    ref = "DVD-S765";
    summary = dvdService.getDvdSummary(ref);
    assertNotNull("Got NULL for ref "+ref, summary);
    assertEquals(ref+" summary mismatch", ref+" Shrek - Big green monsters, they're just all the rage these days...", summary);
  }

  /***
      Acceptance criteria: GREEN
      Time consumed: less than 1 hour.
   ***/

}
