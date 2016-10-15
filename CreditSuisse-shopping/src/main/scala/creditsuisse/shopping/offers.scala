package creditsuisse.shopping
package offers

trait Offer {
  def discount(xs: Seq[Product]): Int
}

class MelonsBuy1Get1Free extends Offer {
  override def discount(xs: Seq[Product]): Int = xs.count(Melon==)/2*Melon.price
}

class Limes3For2 extends Offer {
  override def discount(xs: Seq[Product]): Int = xs.count(Lime==)/3*Lime.price
}
