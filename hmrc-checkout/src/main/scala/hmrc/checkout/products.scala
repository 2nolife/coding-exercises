package hmrc.checkout

sealed trait Product {
  val price: Int // pence
}

case object Apple extends Product {
  override val price = 60
  def unapply(x: String) = if (x.toLowerCase == "apple") Some(Apple) else None
}

case object Orange extends Product {
  override val price = 25
  def unapply(x: String) = if (x.toLowerCase == "orange") Some(Orange) else None
}

object ProductInputParser {
  def parse(xs: Seq[String]): Seq[Product] =
    xs.flatMap {
      case Apple(x) => Some(Apple)
      case Orange(x) => Some(Orange)
      case _ => None
    }
}