package creditsuisse.jeans
package test

import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

class MsProdSpec extends FlatSpec with BeforeAndAfterAll with Matchers with HelperObjects {

  override protected def beforeAll() {
    prodMongoDropDatabase()

    mongoCreateRandomOrders2016()
  }

  it should "populate prod db with data" in {
  }

}

