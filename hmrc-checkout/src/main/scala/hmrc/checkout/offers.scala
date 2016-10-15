package hmrc.checkout
package offers

/** step 2 **/

trait Offer {
  def discount(xs: Seq[Product]): Int
}

class ApplesBuy1Get1Free extends Offer {
  override def discount(xs: Seq[Product]): Int = xs.count(Apple==)/2*Apple.price
}

class Oranges3For2 extends Offer {
  override def discount(xs: Seq[Product]): Int = xs.count(Orange==)/3*Orange.price
}
