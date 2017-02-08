package creditsuisse.jeans
package ms.orders

import akka.actor.ActorSystem
import com.mongodb.casbah.MongoClient
import Constants._
import ms.http.RestClient
import actors.OrdersActor
import ms.orders.rest.OrdersRestService
import db.MongoOrdersDb

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}

object start {

  def createMongoClient(hostname: String, port: Int)(implicit system: ActorSystem, ec: ExecutionContext): MongoClient = {
    val mongoClient = MongoClient(hostname, port)
    system.whenTerminated.onComplete(_ => mongoClient.close())
    mongoClient
  }

  def createRestClient()(implicit system: ActorSystem, ec: ExecutionContext): RestClient = {
    val restClient = new RestClient()
    system.whenTerminated.onComplete(_ => restClient.close())
    restClient
  }

  def main(args: Array[String]) = {
    implicit val system = ActorSystem(s"$APP-$MS")

    sys.addShutdownHook {
      println("Shutting down ...")
      system.terminate()
    }

    run

    Await.result(system.whenTerminated, Duration.Inf)
  }

  def run(implicit system: ActorSystem) {
    implicit val executionContext = system.dispatcher
    val config = Settings(system)

    val mongoClient = createMongoClient(config.mongoDbHostname, config.mongoDbPort)
    val ordersDb = new MongoOrdersDb(mongoClient, config.mongoDbName)

    val restClient = createRestClient()

    val ordersActor = system.actorOf(OrdersActor.props(ordersDb), name = s"$MS-actor")

    new OrdersRestService(config.hostname, config.port, ordersActor, restClient)
  }
}

object Constants {
  val APP = "jeans"
  val MS = "ms-orders"
}
