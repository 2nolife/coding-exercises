package creditsuisse.shopping

sealed trait Product {
  val price: Int // pence
}

case object Melon extends Product {
  override val price = 50
  def unapply(x: String) = if (x.toLowerCase == "melon") Some(Melon) else None
}

case object Banana extends Product {
  override val price = 20
  def unapply(x: String) = if (x.toLowerCase == "banana") Some(Banana) else None
}

case object Apple extends Product {
  override val price = 35
  def unapply(x: String) = if (x.toLowerCase == "apple") Some(Apple) else None
}

case object Lime extends Product {
  override val price = 15
  def unapply(x: String) = if (x.toLowerCase == "lime") Some(Lime) else None
}

object ProductInputParser {
  def parse(xs: Seq[String]): Seq[Product] =
    xs.flatMap {
      case Melon(x) => Some(Melon)
      case Banana(x) => Some(Banana)
      case Apple(x) => Some(Apple)
      case Lime(x) => Some(Lime)
      case _ => None
    }
}