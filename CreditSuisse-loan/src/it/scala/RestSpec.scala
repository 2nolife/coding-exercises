package creditsuisse_loan.rest
package test

import java.util.UUID

import akka.actor.ActorSystem
import akka.http.scaladsl.Http.ServerBinding
import creditsuisse_loan.rest.test.RestClient.HttpCallSuccessful
import org.apache.http.HttpStatus._
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}
import spray.json.{JsObject, _}

import scala.concurrent.Future
import scala.math.BigInt

trait RestServerAndClient {

  val baseUrl = "http://localhost:8080"
  val restClient = new RestClient

  implicit val system = ActorSystem("my-test-system")

  var bindingFuture: Future[ServerBinding] = _

  def startWebServer() {
    bindingFuture = WebServer.start
    Thread.sleep(500)
  }

  def stopWebServer() {
    WebServer.stop(bindingFuture)
    Thread.sleep(500)
  }

}

class RestSpec extends FlatSpec with Matchers with BeforeAndAfterAll with RestServerAndClient {

  implicit def string2jso(s: String): JsObject = s.parseJson.asJsObject

  override def beforeAll() {
    startWebServer()
  }

  override def afterAll() {
    stopWebServer()
  }

  "POST to /api/offers" should "create a new offer and return its id" in {
    restClient.post(
      s"$baseUrl/api/offers",
      OfferEntity(amount = 1000, rate = 8.5).toJson.asJsObject) match {

      case HttpCallSuccessful(_, SC_CREATED, body, _) =>
      // OK

        body.convertTo[OfferCreated].id.length should be > 10

      case x =>
        fail("Received: "+x)
    }
  }

  "POST to /api/loans" should "create a new loan and return its id" in {
    restClient.post(
      s"$baseUrl/api/loans",
      LoanEntity(amount = 600, days = 100).toJson.asJsObject) match {

      case HttpCallSuccessful(_, SC_CREATED, body, _) =>
      // OK

        body.convertTo[LoanCreated].id.length should be > 10

      case x =>
        fail("Received: "+x)
    }
  }

  "GET to /api/offers?loan_id={id}" should "return 404 if loan request id not found" in {
    restClient.get(
      s"$baseUrl/api/offers?loan_id=${UUID.randomUUID}") match {

      case HttpCallSuccessful(_, SC_NOT_FOUND, body, _) =>
      // OK

      case x =>
        fail("Received: "+x)
    }
  }

  "GET to /api/offers?loan_id={id}" should "return current offer" in {
    restClient.post(
      s"$baseUrl/api/offers",
      OfferEntity(amount = 1000, rate = 8.5).toJson.asJsObject) match {
      case HttpCallSuccessful(_, SC_CREATED, body, _) => // OK
      case x => fail("Received: "+x)
    }

    var loanRequestId: String = ""

    restClient.post(
      s"$baseUrl/api/loans",
      LoanEntity(amount = 800, days = 100).toJson.asJsObject) match {
      case HttpCallSuccessful(_, SC_CREATED, body, _) => loanRequestId = body.convertTo[LoanCreated].id // OK
      case x => fail("Received: "+x)
    }

    restClient.get(
      s"$baseUrl/api/offers?loan_id=$loanRequestId") match {

      case HttpCallSuccessful(_, SC_OK, body, _) =>
      // OK

        body.convertTo[MyOffer].amount shouldBe BigInt(800)

      case x =>
        fail("Received: "+x)
    }
  }

}

class AcceptanceTest1RestSpec extends FlatSpec with Matchers with BeforeAndAfterAll with RestServerAndClient {

  implicit def string2jso(s: String): JsObject = s.parseJson.asJsObject

  override def beforeAll() {
    startWebServer()
  }

  override def afterAll() {
    stopWebServer()
  }

  "GET to /api/offers?loan_id={id}" should "return current offer" in {
    restClient.post(
      s"$baseUrl/api/offers",
      OfferEntity(amount = 100, rate = 5.0).toJson.asJsObject) match {
      case HttpCallSuccessful(_, SC_CREATED, body, _) => // OK
      case x => fail("Received: "+x)
    }
    restClient.post(
      s"$baseUrl/api/offers",
      OfferEntity(amount = 500, rate = 8.6).toJson.asJsObject) match {
      case HttpCallSuccessful(_, SC_CREATED, body, _) => // OK
      case x => fail("Received: "+x)
    }

    var loanRequestId: String = ""

    restClient.post(
      s"$baseUrl/api/loans",
      LoanEntity(amount = 1000, days = 100).toJson.asJsObject) match {
      case HttpCallSuccessful(_, SC_CREATED, body, _) => loanRequestId = body.convertTo[LoanCreated].id // OK
      case x => fail("Received: "+x)
    }

    restClient.get(
      s"$baseUrl/api/offers?loan_id=$loanRequestId") match {

      case HttpCallSuccessful(_, SC_OK, body, _) =>
        // OK

        val offer = body.convertTo[MyOffer]
        import offer._
        amount shouldBe BigInt(600)
        rate shouldBe BigDecimal(8.0)

      case x =>
        fail("Received: "+x)
    }
  }

}
