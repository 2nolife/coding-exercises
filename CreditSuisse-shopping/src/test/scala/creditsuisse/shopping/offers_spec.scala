package creditsuisse.shopping
package offers

import org.scalatest._

class OffersSpec extends WordSpec with Matchers {

  "MelonsBuy1Get1Free" should {
    "not discount single melon" in {
      val xs = Melon :: Banana :: Nil
      new MelonsBuy1Get1Free().discount(xs) shouldBe 0
    }

    "discount melons (uneven)" in {
      val xs = Melon :: Banana :: Melon :: Melon :: Nil // 3 melons, 1 free
      new MelonsBuy1Get1Free().discount(xs) shouldBe Melon.price
    }

    "discount melons (even)" in {
      val xs = Melon :: Banana :: Melon :: Melon :: Melon :: Nil // 4 melons, 2 free
      new MelonsBuy1Get1Free().discount(xs) shouldBe Melon.price*2
    }

    "do nothing on empty basket" in {
      new MelonsBuy1Get1Free().discount(Nil) shouldBe 0
    }
  }

  "Limes3For2" should {
    "not discount less than 2 limes" in {
      new Limes3For2().discount(Lime :: Melon :: Nil) shouldBe 0
      new Limes3For2().discount(Lime :: Melon :: Lime :: Nil) shouldBe 0
    }

    "discount 3 or more limes" in {
      new Limes3For2().discount(Lime :: Melon :: Lime :: Lime :: Nil) shouldBe Lime.price // 3 limes, 1 free
      new Limes3For2().discount(Lime :: Melon :: Lime :: Lime :: Melon :: Lime :: Nil) shouldBe Lime.price // 4 limes, 1 free
      new Limes3For2().discount(
        Lime :: Melon :: Lime :: Lime :: Melon :: Lime :: Lime :: Lime :: Nil) shouldBe Lime.price*2 // 6 limes, 2 free
    }

    "do nothing on empty basket" in {
      new Limes3For2().discount(Nil) shouldBe 0
    }
  }
}