package controllers

import play.api.test.Helpers._
import play.api.test._

class EchoControllerSpec extends ControllerTestResources {

  private val echoController = new EchoController(mockCC)

  "EchoController GET /api/echo/{message}" - {

    "return OK on regular expression" in {

      val request = echoController.regex(123).apply(FakeRequest())

      status(request) shouldBe OK
      contentType(request) shouldBe Some("application/json")
      contentAsString(request) should include("123")
    }

    "return OK on text message" in {

      val request = echoController.echo("Text Message").apply(FakeRequest())

      status(request) shouldBe OK
      contentType(request) shouldBe Some("text/html")
      contentAsString(request) should include("Text Message")
    }

  }
}
