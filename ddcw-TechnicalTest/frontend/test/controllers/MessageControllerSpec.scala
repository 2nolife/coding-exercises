package controllers

import connectors.BackendConnector
import play.api.test.Helpers._
import play.api.test._

import scala.concurrent.{ExecutionContext, Future}

class MessageControllerSpec extends ControllerTestResources {

  private val mockBackendConnector = mock[BackendConnector]
  private val messageController = new MessageController(mockBackendConnector, messagesApi, mockCC)

  "MessageController GET /home" - {

    "return OK and show the homepage without a MESSAGE session" in {

      val request = messageController.getHomepage.apply(FakeRequest())

      status(request) shouldBe OK
      contentType(request) shouldBe Some("text/html")
      contentAsString(request) should include("Home")
      contentAsString(request) should not include "Response:"
    }

    "return OK and show the homepage without a MESSAGE session (route)" in {
      val request404 = route(app, FakeRequest(method = "GET", path = "/")).get
      status(request404) shouldBe NOT_FOUND

      val request = route(app, FakeRequest(method = "GET", path = "/home")).get
      status(request) shouldBe OK
      contentType(request) shouldBe Some("text/html")
      contentAsString(request) should include("Home")
      contentAsString(request) should not include "Response:"
    }

    "return OK and show the homepage with a MESSAGE session" in {

      val request = messageController.getHomepage.apply(FakeRequest().withSession("MESSAGE" -> "test1"))

      status(request) shouldBe OK
      contentType(request) shouldBe Some("text/html")
      contentAsString(request) should include("Home")
      contentAsString(request) should include("Response: test1")
    }

    "return OK and show the homepage with a MESSAGE session (route)" in {

      val request = route(app, FakeRequest(method = "GET", path = "/home").withSession("MESSAGE" -> "test1")).get

      status(request) shouldBe OK
      contentType(request) shouldBe Some("text/html")
      contentAsString(request) should include("Home")
      contentAsString(request) should include("Response: test1")
    }
  }

  "TemplateController POST /home" - {

    "redirect to getHomepage with successful form data" in {
      (mockBackendConnector.sendMessage(_: String)(_: ExecutionContext)).expects(*, *).returns(Future successful "response")

      val request = messageController.submitMessage.apply(FakeRequest()
        .withFormUrlEncodedBody(validMessageInputForm: _*))

      status(request) shouldBe SEE_OTHER
      redirectLocation(request).get shouldBe routes.MessageController.getHomepage.url
    }

    "redirect to getHomepage with successful form data (route)" in {
      //(mockBackendConnector.sendMessage(_: String)(_: ExecutionContext)).expects(*, *).returns(Future.successful("response"))

      val request = route(app, FakeRequest(method = "POST", path = controllers.routes.MessageController.submitMessage.path()).withFormUrlEncodedBody(validMessageInputForm: _*)).get

      status(request) shouldBe SEE_OTHER
      redirectLocation(request).get shouldBe routes.MessageController.getHomepage.url
    }

    "return BAD_REQUEST and show form with errors when form data has errors" in {
      val request = messageController.submitMessage.apply(FakeRequest()
        .withFormUrlEncodedBody(invalidMessageInputForm: _*))

      status(request) shouldBe BAD_REQUEST
      contentAsString(request) should include("You must enter a value")
    }

    "return BAD_REQUEST and show form with errors when form data has errors (route)" in {
      val request = route(app, FakeRequest(method = "POST", path = "/home").withFormUrlEncodedBody(invalidMessageInputForm: _*)).get

      status(request) shouldBe BAD_REQUEST
      contentAsString(request) should include("You must enter a value")
    }

    "return BAD_REQUEST and show form with errors when missing form data" in {
      val request = messageController.submitMessage.apply(FakeRequest())

      status(request) shouldBe BAD_REQUEST
    }

    "return BAD_REQUEST and show form with errors when missing form data (route)" in {
      val request = route(app, FakeRequest(method = "POST", path = "/home")).get

      status(request) shouldBe BAD_REQUEST
    }
  }


}

  /*def countFiles(file: File): Int = {
    @tailrec def countFilesAcc(acc: Int, files: List[File]): Int = {
      files match {
        case Nil => acc
        case f :: xs => {
          if (f.isDirectory()) countFilesAcc(acc, f.listFiles().toList ::: xs) else countFilesAcc(acc + 1, xs)
        }
      }
    }

  }*/
