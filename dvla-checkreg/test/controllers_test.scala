package controllers

import java.util.Date

import model.Vehicle
import org.scalatest.BeforeAndAfter
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.test.Helpers._
import play.api.test._
import services.SearchService
import org.mockito.Mockito._
import org.mockito.Matchers._

class ApplicationControllerSpec extends PlaySpec with MockitoSugar with BeforeAndAfter {

  val dummySearchService = mock[SearchService]

  val mockVehicle =
    Vehicle("AE64 RAU", "MINI", new Date, 0, 0, 0, "", "", "", "", "", None, None, None)


  class MockController extends Application {
    override val searchService = dummySearchService
  }

  before {
    when(dummySearchService.search(anyString, anyString)).thenReturn(None)
    when(dummySearchService.search("AE64 RAU", "MINI")).thenReturn(Some(mockVehicle))
  }

  val controller = new MockController

  "Application controller" should {

    "send ok on index page" in {
      val result = controller.index().apply(FakeRequest())
      status(result) mustEqual OK
    }

    "send bad request on invalid search enquiry" in {
      val result = controller.search().apply(FakeRequest(POST, "/search").withFormUrlEncodedBody(("registration", "123"), ("make", "")))
      status(result) mustEqual BAD_REQUEST
      contentAsString(result) must include ("You must enter your registration number in a valid format")
      contentAsString(result) must include ("You must select your vehicle make from the list")
    }

    "send bad request on empty search enquiry" in {
      val result = controller.search().apply(FakeRequest(POST, "/search").withFormUrlEncodedBody(("registration", ""), ("make", "")))
      status(result) mustEqual BAD_REQUEST
      contentAsString(result) must include ("Please enter your registration number")
      contentAsString(result) must include ("You must select your vehicle make from the list")
    }

    "send vehicle data on valid search enquiry" in {
      val result = controller.search().apply(FakeRequest(POST, "/search").withFormUrlEncodedBody(("registration", "AE64 RAU"), ("make", "MINI")))
      status(result) mustEqual OK
      contentAsString(result) must include ("""<h2><span class="registrationNumberLabel">Registration number: </span><span class="registrationNumber">AE64 RAU</span></h2>""")
    }

  }
}
