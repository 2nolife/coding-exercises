package creditsuisse.shopping

import org.scalatest._
import creditsuisse.shopping.offers.MelonsBuy1Get1Free

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
    val xs = Melon :: Lime :: Melon :: Melon :: Nil
    val cost = till.totalCost(xs)
    cost.total shouldBe 50*3+15
  }

  it should "calculate total cost with unknown fruits" in {
    import Till._
    val xs = "Melon" :: "Kiwi" :: "Mango" :: Nil
    val cost = till.totalCost(xs)
    cost.total shouldBe 50
  }
}

class AdvancedTillSpec extends FlatSpec with Matchers {
  val till = new AdvancedTill

  it should "calculate total cost with offers 1" in {
    val xs = Melon :: Lime :: Melon :: Melon :: Nil // 1 melon free
    val cost = till.totalCost(xs)
    cost.total shouldBe Melon.price*3+Lime.price-Melon.price
  }

  it should "calculate total cost with offers 2" in {
    val xs = Melon :: Lime :: Melon :: Melon :: Lime :: Lime :: Nil // 1 melon free, 1 lime free
    val cost = till.totalCost(xs)
    cost.total shouldBe Melon.price*3+Lime.price*3-Melon.price-Lime.price
  }

  it should "calculate total cost with offers 3" in {
    val xs = Melon :: Lime :: Melon :: Melon :: Lime :: Lime :: Lime :: Melon :: Nil // 2 melon free, 1 lime free
    val cost = till.totalCost(xs)
    cost.total shouldBe xs.foldLeft(0)((a,b) => a+b.price)-Melon.price*2-Lime.price
  }

  it should "calculate total cost with offers 4" in {
    val xs = Melon :: Melon :: Melon :: Melon :: Melon :: Melon ::
             Lime :: Lime :: Lime :: Lime :: Lime :: Lime :: Nil // 3 melon free, 2 limes free
    val cost = till.totalCost(xs)
    cost.total shouldBe xs.foldLeft(0)((a,b) => a+b.price)-Melon.price*3-Lime.price*2
  }
}

class CustomTillSpec extends FlatSpec with Matchers {
  val till = new AdvancedTill {
    override val offers = new MelonsBuy1Get1Free :: Nil
  }

  it should "give discount on melons" in {
    val xs = Melon :: Lime :: Melon :: Nil // 1 melon free
    val cost = till.totalCost(xs)
    cost.total shouldBe Melon.price+Lime.price
  }

  it should "not give discount on limes" in {
    val xs = Melon :: Melon :: Lime :: Lime :: Lime :: Lime :: Lime :: Lime :: Nil // 1 melon free
    val cost = till.totalCost(xs)
    cost.total shouldBe Melon.price+Lime.price*6
  }
}

class AdvancedTillAcceptanceSpec extends FlatSpec with Matchers {
  val till = new AdvancedTill

  it should "calculate cost with 1 melon/banana/apple/lime" in {
    till.totalCost(Melon :: Nil).total shouldBe Melon.price
    till.totalCost(Banana :: Nil).total shouldBe Banana.price
    till.totalCost(Apple :: Nil).total shouldBe Apple.price
    till.totalCost(Lime :: Nil).total shouldBe Lime.price
  }

  it should "calculate cost with unique fruits" in {
    val xs = Melon :: Banana :: Apple :: Lime :: Nil
    till.totalCost(xs).total shouldBe Melon.price+Banana.price+Apple.price+Lime.price
  }

  it should "offer on melons and limes" in {
    val xs = Melon :: Banana :: Apple :: Lime :: Melon :: Lime :: Lime :: Nil // 1 melon free, 1 lime free
    till.totalCost(xs).total shouldBe Melon.price+Banana.price+Apple.price+Lime.price*2
  }

  it should "multiple offer on melons and limes" in {
    val xs = Melon :: Banana :: Apple :: Lime :: Melon :: Lime :: Lime ::
             Melon :: Melon :: Lime :: Lime :: Lime :: Nil // 2 melons free, 2 limes free
    till.totalCost(xs).total shouldBe Melon.price*2+Banana.price+Apple.price+Lime.price*4
  }

  it should "zero empty basket" in {
    till.totalCost(Nil).total shouldBe 0
  }
}
