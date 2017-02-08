package creditsuisse.jeans
package ms.orders.rest

import akka.actor.{ActorRef, ActorSystem}
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import ms.http.RestClient
import ms.orders.actors.OrdersActor._

import scala.concurrent.duration._

class OrdersRestService(hostname: String, port: Int,
                        val ordersActor: ActorRef,
                        val restClient: RestClient)(implicit system: ActorSystem)
  extends SprayJsonSupport with HeartbeatRoute with OrdersRoute {

  val log = Logging.getLogger(system, this)

  implicit val executionContext = system.dispatcher
  implicit val materializer = ActorMaterializer()

  implicit val timeout = Timeout(5 seconds)

  val bindingFuture =
    Http().bindAndHandle(Route.handlerFlow(heartbeatRoute ~ ordersRoute), hostname, port)
  log.info(s"Bound Orders REST to $hostname:$port")

  system.whenTerminated.onComplete { _ =>
    bindingFuture.flatMap(_.unbind())
  }

}

trait OrdersRoute {
  self: OrdersRestService =>

  def ordersRoute =
    path("orders" / "top") {
      get {
        parameters('by *) { groupBy =>
          onSuccess(ordersActor ? OrdersIN(groupBy.toSeq)) {
            case OrdersOUT(orders) => complete { (StatusCodes.OK, orders) }
          }
        }
      }
    }

}

trait HeartbeatRoute {

  def heartbeatRoute =
    path("heartbeat") {
      get {
        complete(HttpEntity(ContentTypes.`application/json`, """{"status": "ok"}"""))
      }
    }

}
