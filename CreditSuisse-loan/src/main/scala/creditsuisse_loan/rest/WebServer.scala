package creditsuisse_loan.rest

import java.util.UUID

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import creditsuisse_loan.api.{CurrentOffer, LoanOperation}
import spray.json._

import scala.concurrent.Future
import scala.io.StdIn
import scala.math.BigDecimal.RoundingMode

case class OfferEntity(amount: BigInt, rate: BigDecimal)
object OfferEntity extends DefaultJsonProtocol { implicit val format = jsonFormat2(apply) }

case class OfferCreated(id: String)
object OfferCreated extends DefaultJsonProtocol { implicit val format = jsonFormat1(apply) }

case class LoanEntity(amount: BigInt, days: BigInt)
object LoanEntity extends DefaultJsonProtocol { implicit val format = jsonFormat2(apply) }

case class LoanCreated(id: String)
object LoanCreated extends DefaultJsonProtocol { implicit val format = jsonFormat1(apply) }

case class MyOffer(amount: BigInt, rate: BigDecimal)
object MyOffer extends DefaultJsonProtocol {
  implicit val format = jsonFormat2(apply)
  def apply(offer: CurrentOffer): MyOffer = new MyOffer(offer.amount, offer.rate.setScale(4, RoundingMode.HALF_UP))
}

object WebServer {

  def main(args: Array[String]) {
    implicit val system = ActorSystem("my-system")

    val bindingFuture = start

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()

    stop(bindingFuture)
  }

  def start(implicit system: ActorSystem): Future[ServerBinding] = {

    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val loanOp = new LoanOperation(() => UUID.randomUUID)

    implicit def uuid2string(uuid: UUID): String = uuid.toString
    implicit def string2uuid(s: String): UUID = UUID.fromString(s)

    val route =
      path("api" / "offers") {
        post {
          entity(as[OfferEntity]) { jso =>
            import jso._
            val id = loanOp.addOffer(amount, rate)
            complete {
              (StatusCodes.Created, OfferCreated(id))
            }
          }
        } ~
        get {
          parameters('loan_id) { loanRequestId =>
            loanOp.currentOffer(loanRequestId) match {
              case Some(offer) => complete { (StatusCodes.OK, MyOffer(offer)) }
              case _ => complete { StatusCodes.NotFound }
            }

          }
        }
      } ~
      path("api" / "loans") {
        post {
          entity(as[LoanEntity]) { jso =>
            import jso._
            val id = loanOp.createLoanRequest(amount, days)
            complete {
              (StatusCodes.Created, LoanCreated(id))
            }
          }
        }
      }

    Http().bindAndHandle(route, "localhost", 8080)
  }

  def stop(bindingFuture: Future[ServerBinding])(implicit system: ActorSystem) {
    implicit val executionContext = system.dispatcher
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ ⇒ system.terminate())
  }
}
