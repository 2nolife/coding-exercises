package creditsuisse.jeans
package ms.orders.db

import ms.db.MongoQueries
import ms.orders.Constants._
import ms.orders.vo
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.{Cursor, MongoClient}
import com.mongodb.casbah.commons.MongoDBObject

trait OrdersDb extends {
  def topOrders(groupBy: Seq[String]): Seq[vo.OrderSummary]
}

class MongoOrdersDb(client: MongoClient, dbName: String) extends OrdersDb with MongoQueries {

  private val db = client(dbName)
  val orders = db(MS)

  def asOrderSummary(data: MongoDBObject): vo.OrderSummary = {
    val fields = data.as[MongoDBObject]("_id")
    val count = data.getAs[Int]("count")

    import fields._
    vo.OrderSummary(
      year = getAs[Int]("year"),
      month = getAs[Int]("month"),
      day = getAs[Int]("day"),
      country = getAs[String]("country"),
      manufacturer = getAs[String]("manufacturer"),
      gender = getAs[String]("gender"),
      size = getAs[String]("size"),
      colour = getAs[String]("colour"),
      style = getAs[String]("style"),
      count = count)
  }

  def top10(groupBy: Seq[String]): Seq[vo.OrderSummary] = {
    val fields = groupBy.map(field => field -> ("$"+field)).toList
    val aggregationOptions = AggregationOptions(AggregationOptions.CURSOR)

    val result =
      orders.aggregate(
        List(
          MongoDBObject("$group" ->
            MongoDBObject(
              "_id" -> MongoDBObject(fields),
              "count" -> MongoDBObject("$sum" -> "$count")
            )
          ),
          MongoDBObject("$sort" -> MongoDBObject("count" -> -1)),
          MongoDBObject("$limit" -> 10)
        ),
        aggregationOptions
      )

    result.map(asOrderSummary(_)).toSeq
  }

  override def topOrders(groupBy: Seq[String]): Seq[vo.OrderSummary] =
    top10(groupBy)

}
