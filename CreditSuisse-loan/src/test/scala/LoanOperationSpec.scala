package creditsuisse_loan.api
package test

import java.util.UUID

import org.scalatest._
import org.scalatest.mock.MockitoSugar

class LoanOperationSpec extends FlatSpec with MockitoSugar with Matchers with BeforeAndAfter {

  var loanOp: LoanOperation = _
  val ids = (1 to 10).map(_ => UUID.randomUUID)
  var idsIt: Iterator[UUID] = _

  before {
    idsIt = ids.iterator
    loanOp = new LoanOperation(() => idsIt.next())
  }

  it should "create a loan request and return its id" in {
    loanOp.createLoanRequest(amount = 1000, days = 100) shouldBe ids(0)
    loanOp.loanRequestsSize shouldBe 1
  }

  it should "reject invalid loan requests" in {
    an [IllegalArgumentException] should be thrownBy loanOp.createLoanRequest(amount = 0, days = 100)
    an [IllegalArgumentException] should be thrownBy loanOp.createLoanRequest(amount = 1000, days = 0)
  }

  it should "add an offer and return it id" in {
    loanOp.addOffer(amount = 1000, rate = 8.6) shouldBe ids(0)
    loanOp.offersSize shouldBe 1
  }

  it should "reject invalid offers" in {
    an [IllegalArgumentException] should be thrownBy loanOp.addOffer(amount = 0, rate = 8.6)
    an [IllegalArgumentException] should be thrownBy loanOp.addOffer(amount = 1000, rate = 0)
  }

  it should "create a loan offer and return its id" in {
    loanOp.createLoanOffer(loanRequestId = UUID.randomUUID(), amount = 500, rate = 3.0) shouldBe ids(0)
    loanOp.loanOffersSize shouldBe 1
  }

  it should "reject invalid loan offers" in {
    an [IllegalArgumentException] should be thrownBy loanOp.createLoanOffer(loanRequestId = UUID.randomUUID(), amount = 0, rate = 3.0)
    an [IllegalArgumentException] should be thrownBy loanOp.createLoanOffer(loanRequestId = UUID.randomUUID(), amount = 500, rate = 0)
  }

  "current offer" should "return none on invalid loan request id" in {
    loanOp.currentOffer(loanRequestId = UUID.randomUUID()) shouldBe None
  }

  "acceptance test 1, current offer" should "return amount and rate" in {
    loanOp.addOffer(amount = 100, rate = 5.0)
    loanOp.addOffer(amount = 500, rate = 8.6)
    val loanRequestId = loanOp.createLoanRequest(amount = 1000, days = 100)
    val offer = loanOp.currentOffer(loanRequestId)
    offer should not be None
    offer.get shouldBe CurrentOffer(amount = 600, rate = 8.0)
  }

  "acceptance test 2, current offer" should "return amount and rate" in {
    loanOp.addOffer(amount = 100, rate = 5.0)
    loanOp.addOffer(amount = 600, rate = 6.0)
    loanOp.addOffer(amount = 600, rate = 7.0)
    loanOp.addOffer(amount = 500, rate = 8.2)
    val loanRequestId = loanOp.createLoanRequest(amount = 1000, days = 100)
    val offer = loanOp.currentOffer(loanRequestId)
    offer should not be None
    offer.get shouldBe CurrentOffer(amount = 1000, rate = 6.2)
  }

  "edge case, current offer" should "return none if no offers added" in {
    val loanRequestId = loanOp.createLoanRequest(amount = 1000, days = 100)
    loanOp.currentOffer(loanRequestId) shouldBe None
  }
}
