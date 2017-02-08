package creditsuisse.jeans
package ms.orders.actors

import akka.actor.{Actor, ActorLogging, Props}
import ms.orders.db.OrdersDb
import ms.orders.vo

object OrdersActor {
  def props(ordersDb: OrdersDb): Props = Props(new OrdersActor(ordersDb))

  case class OrdersIN(groupBy: Seq[String])
  case class OrdersOUT(orders: Seq[vo.OrderSummary])
}

class OrdersActor(ordersDb: OrdersDb) extends Actor with ActorLogging {
  import OrdersActor._

  def receive = {

    case OrdersIN(groupBy) =>
      val orders = if (groupBy.nonEmpty) ordersDb.topOrders(groupBy) else Nil

      sender ! OrdersOUT(orders)

  }

}
