package com.coldcore.ex.numtoword

object WordWriter {

  val MIN = 0
  val MAX = 99000000000L

  private val nilTo19 = Array[String](
          "nil",
          "one",
          "two",
          "three",
          "four",
          "five",
          "six",
          "seven",
          "eight",
          "nine",
          "ten",
          "eleven",
          "twelve",
          "thirteen",
          "fourteen",
          "fifteen",
          "sixteen",
          "seventeen",
          "eighteen",
          "nineteen"
  )

  private val tens = Array[String](
           "ten",
           "twenty",
           "thirty",
           "forty",
           "fifty",
           "sixty",
           "seventy",
           "eighty",
           "ninety"
   )

  private val powers = Array[String](
           "hundred", "thousand", "million", "billion"
   )

  /* 12345 -> 12 345 */
  def asTokens(n: Long): List[Int] =
    n.toString.reverse.grouped(3).toList.reverse map(x => Integer.parseInt(x.reverse))

  def asWords(n: Long): String = {
    /* 345 -> three hundred forty five */
    def word(t: Int): String = t match {
      case v if (v == 0) => ""
      case v if (v < 20) => nilTo19(v)
      case v if (v < 100) => tens(v/10-1)+" "+word(v%10)
      case v if (v >= 100) => word(v/100)+" "+powers(0)+(if (v%100 == 0) "" else " "+word(v%100))
      case _ => ""
    }

    /* 12 -> twelve thousand */
    val pows = (x: Int, i: Int) =>
      if (i > 0) " "+powers(i)+(if (i == 2 && x > 1) "s" else "") else ""

    n match {
      case _ if (n < MIN) => throw new IllegalArgumentException("Argument is less than "+MIN)
      case _ if (n > MAX) => throw new IllegalArgumentException("Argument is greater than "+MAX)
      case 0 => nilTo19(0)
      case _ =>
        val tuples = asTokens(n).reverse.zipWithIndex.reverse
        tuples filter(_._1 > 0) map(t => word(t._1)+pows(t._1, t._2)) mkString(" ")
    }
  }

}