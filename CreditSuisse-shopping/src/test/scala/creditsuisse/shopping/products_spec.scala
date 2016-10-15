package creditsuisse.shopping

import org.scalatest._

class ProductSpec extends FlatSpec with Matchers {

  "Parser" should "parse user input" in {
    val xs = "Melon" :: "lime" :: "APPLE" :: "melon" :: "Banana" :: "Apple" :: Nil
    val rs = ProductInputParser.parse(xs)
    rs shouldBe List(Melon, Lime, Apple, Melon, Banana, Apple)
  }

  "Parser" should "parse user input with unknown fruits" in {
    val xs = "Melon" :: "Kiwi" :: "Mango" :: Nil
    val rs = ProductInputParser.parse(xs)
    rs shouldBe List(Melon)
  }
}