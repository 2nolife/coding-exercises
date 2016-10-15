package hmrc.checkout
package offers

import org.scalatest._

/** step 2 **/

class OffersSpec extends WordSpec with Matchers {

  "ApplesBuy1Get1Free" should {
    "not discount single apple" in {
      val xs = Apple :: Orange :: Nil
      new ApplesBuy1Get1Free().discount(xs) shouldBe 0
    }

    "discount apples (uneven)" in {
      val xs = Apple :: Orange :: Apple :: Apple :: Nil // 3 apples, 1 free
      new ApplesBuy1Get1Free().discount(xs) shouldBe Apple.price
    }

    "discount apples (even)" in {
      val xs = Apple :: Orange :: Apple :: Apple :: Apple :: Nil // 4 apples, 2 free
      new ApplesBuy1Get1Free().discount(xs) shouldBe Apple.price*2
    }

    "do nothing on empty basket" in {
      new ApplesBuy1Get1Free().discount(Nil) shouldBe 0
    }
  }

  "Oranges3For2" should {
    "not discount less than 2 oranges" in {
      new Oranges3For2().discount(Orange :: Apple :: Nil) shouldBe 0
      new Oranges3For2().discount(Orange :: Apple :: Orange :: Nil) shouldBe 0
    }

    "discount 3 or more oranges" in {
      new Oranges3For2().discount(Orange :: Apple :: Orange :: Orange :: Nil) shouldBe Orange.price // 3 oranges, 1 free
      new Oranges3For2().discount(Orange :: Apple :: Orange :: Orange :: Apple :: Orange :: Nil) shouldBe Orange.price // 4 oranges, 1 free
      new Oranges3For2().discount(
        Orange :: Apple :: Orange :: Orange :: Apple :: Orange :: Orange :: Orange :: Nil) shouldBe Orange.price*2 // 6 oranges, 2 free
    }

    "do nothing on empty basket" in {
      new Oranges3For2().discount(Nil) shouldBe 0
    }
  }
}