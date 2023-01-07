package com.coldcore.ex.numtoword

import WordWriter._
import org.scalatest.Suite

class WordWriterTest extends Suite {

  def testTokens() {
    expect(List(0)) { asTokens(0) }
    expect(List(1)) { asTokens(1) }
    expect(List(12)) { asTokens(12) }
    expect(List(12, 345)) { asTokens(12345) }
    expect(List(12, 345, 678)) { asTokens(12345678) }
    expect(List(123, 456, 789)) { asTokens(123456789) }
  }

  def testWords() {
    expect("nil") { asWords(0) }
    expect("one") { asWords(1) }
    expect("sixty five") { asWords(65) }
    expect("one hundred") { asWords(100) }
    expect("one hundred eighteen") { asWords(118) }
    expect("one thousand three hundred sixteen") { asWords(1316) }
    expect("one million") { asWords(1000000) }
    expect("two millions") { asWords(2000000) }
    expect("three millions two hundred") { asWords(3000200) }
    expect("seven hundred thousand") { asWords(700000) }
    expect("nine millions one thousand") { asWords(9001000) }
    expect("one hundred twenty three millions four hundred fifty six thousand seven hundred eighty nine") { asWords(123456789) }
    expect("two billion one hundred forty seven millions four hundred eighty three thousand six hundred forty seven") { asWords(2147483647) }
    expect("three billion ten") { asWords(3000000010L) }
  }

}