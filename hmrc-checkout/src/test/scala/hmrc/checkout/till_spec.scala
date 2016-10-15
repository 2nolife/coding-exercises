package hmrc.checkout

import org.scalatest._
import hmrc.checkout.offers.ApplesBuy1Get1Free

class CostSpec extends FlatSpec with Matchers {

  it should "print receipt" in {
    Cost(123, "GBP").receipt shouldBe "GBP1.23"
    Cost(23, "GBP").receipt shouldBe "GBP0.23"
    Cost(0).receipt shouldBe "£0.00"
    Cost(1098).receipt shouldBe "£10.98"
    Cost(1090).receipt shouldBe "£10.90"
  }
}

class SimpleTillSpec extends FlatSpec with Matchers {
  val till = new SimpleTill

  it should "calculate total cost" in {
    val xs = Apple :: Orange :: Apple :: Apple :: Nil
    val cost = till.totalCost(xs)
    cost.total shouldBe 60*3+25
  }

  it should "calculate total cost with unknown fruits" in {
    import Till._
    val xs = "Apple" :: "Kiwi" :: "Banana" :: Nil
    val cost = till.totalCost(xs)
    cost.total shouldBe 60
  }
}

/** step 2 **/

class AdvancedTillSpec extends FlatSpec with Matchers {
  val till = new AdvancedTill

  it should "calculate total cost with offers 1" in {
    val xs = Apple :: Orange :: Apple :: Apple :: Nil // 1 apple free
    val cost = till.totalCost(xs)
    cost.total shouldBe Apple.price*3+Orange.price-Apple.price
  }

  it should "calculate total cost with offers 2" in {
    val xs = Apple :: Orange :: Apple :: Apple :: Orange :: Orange :: Nil // 1 apple free, 1 orange free
    val cost = till.totalCost(xs)
    cost.total shouldBe Apple.price*3+Orange.price*3-Apple.price-Orange.price
  }

  it should "calculate total cost with offers 3" in {
    val xs = Apple :: Orange :: Apple :: Apple :: Orange :: Orange :: Orange :: Apple :: Nil // 2 apple free, 1 orange free
    val cost = till.totalCost(xs)
    cost.total shouldBe xs.foldLeft(0)((a,b) => a+b.price)-Apple.price*2-Orange.price
  }

  it should "calculate total cost with offers 4" in {
    val xs = Apple :: Apple :: Apple :: Apple :: Apple :: Apple ::
             Orange :: Orange :: Orange :: Orange :: Orange :: Orange :: Nil // 3 apple free, 2 oranges free
    val cost = till.totalCost(xs)
    cost.total shouldBe xs.foldLeft(0)((a,b) => a+b.price)-Apple.price*3-Orange.price*2
  }
}

class CustomTillSpec extends FlatSpec with Matchers {
  val till = new AdvancedTill {
    override val offers = new ApplesBuy1Get1Free :: Nil
  }

  it should "give discount on apples" in {
    val xs = Apple :: Orange :: Apple :: Nil // 1 apple free
    val cost = till.totalCost(xs)
    cost.total shouldBe Apple.price+Orange.price
  }

  it should "not give discount on oranges" in {
    val xs = Apple :: Apple :: Orange :: Orange :: Orange :: Orange :: Orange :: Orange :: Nil // 1 apple free
    val cost = till.totalCost(xs)
    cost.total shouldBe Apple.price+Orange.price*6
  }
}
