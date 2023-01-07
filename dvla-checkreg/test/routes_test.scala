import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.test.Helpers._
import play.api.test._

class RoutesSpec extends PlaySpec with MockitoSugar with OneAppPerSuite {

  "Application routes" should {

    "render custom 404 on a bad request" in {
      val Some(result) = route(FakeRequest(GET, "/boum"))
      status(result) mustEqual NOT_FOUND
      contentAsString(result) must include ("<h1>Page not found</h1>")
    }

    "render index page" in {
      val home = route(FakeRequest(GET, "/")).get

      status(home) mustEqual OK
      contentType(home) mustEqual Some("text/html")
      charset(home) mustEqual Some("utf-8")
      contentAsString(home) must include ("<h1>Get vehicle information from DVLA</h1>")
    }

    "render index page with other url" in {
      val home = route(FakeRequest(GET, "/get-vehicle-information-from-dvla")).get

      status(home) mustEqual OK
      contentAsString(home) must include ("<h1>Get vehicle information from DVLA</h1>")
    }

    "render enquiry page" in {
      val home = route(FakeRequest(GET, "/enquiry")).get

      status(home) mustEqual OK
      contentAsString(home) must include ("<h2>Details of the vehicle being checked</h2>")
    }

    "render enquiry page when invalid data entered into search" in {
      val page = route(FakeRequest(POST, "/search").withFormUrlEncodedBody(("registration", "123"), ("make", ""))).get

      status(page) mustEqual BAD_REQUEST
      contentAsString(page) must include ("You must enter your registration number in a valid format")
      contentAsString(page) must include ("You must select your vehicle make from the list")
    }

    "render vehicle not found page" in {
      val page = route(FakeRequest(POST, "/search").withFormUrlEncodedBody(("registration", "AE64 ABC"), ("make", "VOLVO"))).get

      status(page) mustEqual OK
      contentAsString(page) must include ("""<h3 xmlns:utils="urn:utils" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">Vehicle details could not be found</h3>""")
    }

    "render vehicle found page" in {
      val page = route(FakeRequest(POST, "/search").withFormUrlEncodedBody(("registration", "AE64 RAU"), ("make", "MINI"))).get

      status(page) mustEqual OK
      contentAsString(page) must include ("""<h2><span class="registrationNumberLabel">Registration number: </span><span class="registrationNumber">AE64 RAU</span></h2>""")
    }

    "render finish page" in {
      val page = route(FakeRequest(GET, "/finish")).get

      status(page) mustEqual OK
      contentAsString(page) must include ("<h1>Thank you</h1>")
    }
  }
}
