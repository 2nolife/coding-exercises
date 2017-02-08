package creditsuisse.jeans
package test

import org.apache.http.HttpStatus._
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}
import ms.orders.vo

class MsOrdersTopSpec extends FlatSpec with BeforeAndAfterAll with Matchers with HelperObjects {

  override protected def beforeAll() {
    systemStart()

    mongoDropDatabase()

    mongoCreateTestOrders()
  }

  override protected def afterAll() {
    systemStop()
  }

  "GET /orders/top" should "return an empty list if no criteria is specified" in {
    val url = s"$ordersBaseUrl/orders/top"
    val orders = (When getTo url expect() code SC_OK).withBody[Seq[vo.OrderSummary]]

    orders.size shouldBe 0
  }

  "GET /orders/top" should "list orders grouped by a manufacturer and gender" in {
    val url = s"$ordersBaseUrl/orders/top?by=manufacturer&by=gender"
    val orders = (When getTo url expect() code SC_OK).withBody[Seq[vo.OrderSummary]]

    val expected =
      ("True Religion", "M", 10) ::
      ("Denzil Jeans", "M", 7) ::
      ("The Hipster Jeans Company", "M", 6) ::
      ("Wrangled Jeans", "F", 4) ::
      ("The Hipster Jeans Company", "F", 3) ::
      ("True Religion", "F", 1) ::
      Nil

    orders.size shouldBe 6
    orders.map { order => import order._; (manufacturer.get, gender.get, count.get) } should contain theSameElementsInOrderAs expected
  }

}

