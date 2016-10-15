package hmrc.checkout

import org.scalatest._

class ProductSpec extends FlatSpec with Matchers {

  "Parser" should "parse user input" in {
    val xs = "Apple" :: "Orange" :: "APPLE" :: "apple" :: Nil
    val rs = ProductInputParser.parse(xs)
    rs shouldBe List(Apple, Orange, Apple, Apple)
  }

  "Parser" should "parse user input with unknown fruits" in {
    val xs = "Apple" :: "Kiwi" :: "Banana" :: Nil
    val rs = ProductInputParser.parse(xs)
    rs shouldBe List(Apple)
  }
}