package creditsuisse.jeans
package test

import java.util.{Calendar, GregorianCalendar}

import akka.actor.ActorSystem
import ms.http.RestClient.{HttpCall, HttpCallSuccessful}
import ms.http.RestClient
import ms._
import ms.db.MongoQueries
import ms.orders.Constants.APP
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.{MongoClient, MongoCollection}
import spray.json.{JsObject, _}
import org.apache.http.HttpStatus._
import org.bson.types.ObjectId
import org.scalatest.exceptions.TestFailedException

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

/** Common trait to include into the integration tests. */
trait HelperObjects extends MongoOps with SystemStart with RestClientOps with RestClientDSL with FilesReader {

  implicit def string2jso(s: String): JsObject = s.parseJson.asJsObject

}

/** Read files from resources directory. */
trait FilesReader {

  def readFileAsString(path: String): String = {
    val stream = getClass.getResourceAsStream(path)
    scala.io.Source.fromInputStream(stream).mkString
  }

}

/** REST client and authorization headers. */
trait RestClientOps {

  val restClient = new RestClient

  /** Check a response code and return its body:
    *   restClient.post(url, json, Authorization.name, "Bearer ABCDEF").expectWithCode(SC_CREATED).convertTo[TokenRemote]
    */
  implicit class ExpectedResponse(httpCall: HttpCall) {
    def expectWithCode(code: Int): JsValue = httpCall match {
      case HttpCallSuccessful(_, c, body, _) if c == code => body
      case x => throw new TestFailedException(s"Received: $x", 4)
    }
  }

}

/** Start and stop the system. */
trait SystemStart extends BaseURLs {

  implicit val system = ActorSystem(APP)

  sys.addShutdownHook {
    println("Shutting down ...")
    system.terminate()
  }

  def systemStart() {
    orders.start.run

    // wait till all micro services start
    val restClient = new RestClient
    implicit val executionContext = system.dispatcher
    def started: Boolean = baseUrls.map(url => isStarted(url, restClient)).forall(true==)
    Await.result(Future { while (!started) Thread.sleep(100) }, 20 seconds)
  }

  def systemStop() {
    system.terminate()
    Await.result(system.whenTerminated, Duration.Inf)
  }

  private def isStarted(baseUrl: String, restClient: RestClient): Boolean =
    restClient.get(s"$baseUrl/heartbeat") match {
      case HttpCallSuccessful(_, SC_OK, _, _) => true
      case _ => false
    }

}

/** Micro services base URLs. */
trait BaseURLs {

  val ordersBaseUrl = "http://localhost:8021"

  val baseUrls =
    ordersBaseUrl :: Nil
}

/** MongoDB client and operations. */
trait MongoOps extends MongoQueries with MongoTables with MongoCreate with MongoCleaner {
  lazy val mongoClient = MongoClient("localhost", 27017)
  lazy val mongoDB = mongoClient(APP+"-test")
  lazy val prodMongoDB = mongoClient(APP)

  def randomId: String = ObjectId.get.toString
}

/** MongoDB collections. */
trait MongoTables {
  self: MongoOps =>

  lazy val mongoOrders: MongoCollection = mongoDB(orders.Constants.MS)
  lazy val prodMongoOrders: MongoCollection = prodMongoDB(orders.Constants.MS)
}

/** Create objects in MongoDB. */
trait MongoCreate {
  self: MongoOps =>

  def mongoCreateOrder(year: Int, month: Int, day: Int,
                       country: String, manufacturer: String, gender: String,
                       size: String, colour: String, style: String, count: Int,
                       mongoOrders: MongoCollection = mongoOrders): String = {
    val order = MongoDBObject(
      "test" -> true,
      "year" -> year,
      "month" -> month,
      "day" -> day,
      "country" -> country,
      "manufacturer" -> manufacturer,
      "gender" -> gender,
      "size" -> size,
      "colour" -> colour,
      "style" -> style,
      "count" -> count)

    mongoOrders
      .insert(order)

    order.idString
  }

  def mongoCreateTestOrders() = {
    mongoCreateOrder(2016, 1, 1, "Germany",        "The Hipster Jeans Company", "F", "16",    "Dark Blue",  "Relaxed",  3)
    mongoCreateOrder(2016, 1, 1, "United Kingdom", "Denzil Jeans",              "M", "32/32", "Light Blue", "Skinny",   7)
    mongoCreateOrder(2016, 1, 2, "France",         "The Hipster Jeans Company", "M", "28/30", "Red",        "Skinny",   6)
    mongoCreateOrder(2016, 1, 2, "Austria",        "Wrangled Jeans",            "F", "12",    "Yellow",     "Boot Cut", 1)

    mongoCreateOrder(2016, 1, 3, "France",  "True Religion",  "M", "28/30", "Blue",   "Skinny",   4)
    mongoCreateOrder(2016, 1, 3, "France",  "True Religion",  "M", "28/30", "Blue",   "Skinny",   2)
    mongoCreateOrder(2016, 1, 3, "Austria", "True Religion",  "M", "28/30", "Blue",   "Skinny",   4)
    mongoCreateOrder(2016, 1, 3, "Austria", "True Religion",  "F", "28/30", "Blue",   "Skinny",   1)
    mongoCreateOrder(2016, 1, 3, "Austria", "Wrangled Jeans", "F", "12",    "Yellow", "Boot Cut", 1)
    mongoCreateOrder(2016, 1, 3, "Germany", "Wrangled Jeans", "F", "12",    "Yellow", "Boot Cut", 1)
    mongoCreateOrder(2016, 1, 3, "Germany", "Wrangled Jeans", "F", "14",    "Red",    "Relaxed",  1)
  }

  def mongoCreateRandomOrders2016() = {
    val countries = "Germany" :: "United Kingdom" :: "France" :: "Austria" :: "United States" :: "Russia" :: Nil
    val manufacturers = "The Hipster Jeans Company" :: "Denzil Jeans" :: "Wrangled Jeans" :: "Lewis" :: "Denim Jeans" :: "True Religion" :: Nil
    val genders = "M" :: "F" :: Nil
    val sizes = "12" :: "14" :: "16" :: "30/32" :: "32/32" :: "32/34" :: "34/34" :: Nil
    val colours = "Dark Blue" :: "Light Blue" :: "Red" :: "Yellow" :: "Deep Blue" :: "Black" :: "White" :: Nil
    val styles = "Relaxed" :: "Skinny" :: "Boot Cut" :: "Normal" :: "Stone washed" :: Nil
    val counts = 1 to 25

    def randomOf[T](xs: Seq[T]): T = xs((math.random*xs.size.toDouble).toInt)

    val calendar = new GregorianCalendar(2016, 0, 1)

    (1 to 365).foreach { dayOfYear =>
      if (calendar.get(Calendar.YEAR) == 2016) {
        countries.foreach { country =>
          (1 to 5).foreach {
            mongoCreateOrder(
              calendar.get(Calendar.YEAR),
              calendar.get(Calendar.MONTH)+1,
              calendar.get(Calendar.DAY_OF_MONTH),
              randomOf(countries),
              randomOf(manufacturers),
              randomOf(genders),
              randomOf(sizes),
              randomOf(colours),
              randomOf(styles),
              randomOf(counts),
              mongoOrders = prodMongoOrders)
          }
        }
        calendar.add(Calendar.DAY_OF_MONTH, 1)
      }
    }

  }

}

/** Delete objects from MongoDB. */
trait MongoCleaner {
  self: MongoOps =>

  def mongoDropDatabase() =
    mongoDB.dropDatabase()

  def prodMongoDropDatabase() =
    prodMongoDB.dropDatabase()

}

/** REST DSL
  * Examples:
  *   When.postTo(url).entity(json).withHeaders(headers).expect.code(SC_CREATED).withBody[TokenRemote]
  *   val token = (When postTo url entity json withHeaders headers expect() code SC_CREATED).withBody[TokenRemote]
  *   When postTo url entity json withHeaders headers expect() code SC_CREATED
  */
trait RestClientDSL {

  private val restClient = new RestClient
  implicit private def string2jso(s: String): JsObject = s.parseJson.asJsObject

  case class Values(method: Symbol = null, url: String = null, json: JsObject = null,
                    headers: Seq[(String,String)] = Nil,
                    restCallResult: Option[HttpCall] = None)

  case object When {
    def postTo(url: String): Post = Post(Values(url = url, method = 'post))
    def getTo(url: String): Get = Get(Values(url = url, method = 'get))
    def putTo(url: String): Put = Put(Values(url = url, method = 'put))
    def deleteTo(url: String): Delete = Delete(Values(url = url, method = 'delete))
    def patchTo(url: String): Patch = Patch(Values(url = url, method = 'patch))
  }

  case class Post(values: Values) {
    def entity(json: JsObject): Entity = Entity(values.copy(json = json))
  }

  case class Put(values: Values) {
    def entity(json: JsObject): Entity = Entity(values.copy(json = json))
  }

  case class Patch(values: Values) {
    def entity(json: JsObject): Entity = Entity(values.copy(json = json))
  }

  case class Get(values: Values) {
    def expect(): Expect = Expect(values)
    def withHeaders(headers: Seq[(String,String)]): Headers = Headers(values.copy(headers = headers))
  }

  case class Delete(values: Values) {
    def expect(): Expect = Expect(values)
    def withHeaders(headers: Seq[(String,String)]): Headers = Headers(values.copy(headers = headers))
  }

  case class Entity(values: Values) {
    def expect(): Expect = Expect(values)
    def withHeaders(headers: Seq[(String,String)]): Headers = Headers(values.copy(headers = headers))
  }

  case class Headers(values: Values) {
    def expect(): Expect = Expect(values)
  }

  case class Expect(values: Values) {
    import values._
    lazy val httpCall =
      restCallResult.getOrElse {
        values.method match {
          case 'post => restClient.post(url, json, headers: _*)
          case 'put => restClient.put(url, json, headers: _*)
          case 'patch => restClient.patch(url, json, headers: _*)
          case 'get => restClient.get(url, headers: _*)
          case 'delete => restClient.delete(url, headers: _*)
        }
      }
    lazy val nvalues = values.copy(restCallResult = Some(httpCall))

    def code(code: Int): Expect =
      httpCall match {
        case HttpCallSuccessful(_, c, _, _) if c == code => Expect(nvalues)
        case x => throw new TestFailedException(s"Received: $x", 4)
      }

    def withBody[T : JsonReader]: T =
      httpCall match {
        case HttpCallSuccessful(_, _, body, _) => body.convertTo[T]
        case x => throw new TestFailedException(s"Received: $x", 4)
      }
  }

}

