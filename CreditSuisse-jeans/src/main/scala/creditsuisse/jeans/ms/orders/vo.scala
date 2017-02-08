package creditsuisse.jeans
package ms.orders.vo

import spray.json.DefaultJsonProtocol

case class OrderSummary(year: Option[Int], month: Option[Int], day: Option[Int],
                        country: Option[String], manufacturer: Option[String], gender: Option[String],
                        size: Option[String], colour: Option[String], style: Option[String], count: Option[Int])
object OrderSummary extends DefaultJsonProtocol { implicit val format = jsonFormat10(apply) }
