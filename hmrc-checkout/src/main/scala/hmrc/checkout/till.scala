package hmrc.checkout

import hmrc.checkout.offers.{Offer, ApplesBuy1Get1Free, Oranges3For2}

case class Cost(total: Int, currency: String = "£") {
  def receipt = currency+((total/100)+"."+(total-total/100*100)).padTo(4, "0").mkString // £2.35
}

trait Till {
  def totalCost(xs: Seq[Product]): Cost
}

object Till {
  implicit def toProducts(xs: Seq[String]): Seq[Product] = ProductInputParser.parse(xs)
}

class SimpleTill extends Till {
  override def totalCost(xs: Seq[Product]): Cost =
    Cost((0 /: xs)((a,b) => a+b.price))
}

/** step 2 **/

trait Discount {
  val offers: Seq[Offer]
  def discount(xs: Seq[Product]): Int = offers.map(_.discount(xs)).sum
}

class AdvancedTill extends SimpleTill with Discount {
  override val offers = new ApplesBuy1Get1Free :: new Oranges3For2 :: Nil
  override def totalCost(xs: Seq[Product]): Cost =
    super.totalCost(xs) match { case c => c.copy(total = c.total-discount(xs)) }
}

