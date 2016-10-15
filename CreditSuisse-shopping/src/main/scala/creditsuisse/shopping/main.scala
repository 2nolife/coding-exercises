package creditsuisse.shopping

import Till._

object main {

  def main(args: Array[String]) {
    val cost = new AdvancedTill().totalCost(args.toSeq)
    println(s"Items: ${args.mkString(" ")}")
    println(s"Receipt: ${cost.receipt}")
    println("Thank you, come again!")
  }

}