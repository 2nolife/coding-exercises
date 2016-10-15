package creditsuisse_loan.api

import java.util.UUID
import java.util.concurrent.locks.ReentrantLock

import scala.collection._

case class LoanRequest(id: UUID, amount: BigInt, days: BigInt)
case class LoanOffer(id: UUID, loanRequestId: UUID, amount: BigInt, rate: BigDecimal)
case class Offer(id: UUID, amount: BigInt, rate: BigDecimal)

case class CurrentOffer(amount: BigInt, rate: BigDecimal)
case object CurrentOffer {
  def apply(offer: LoanOffer): CurrentOffer = new CurrentOffer(offer.amount, offer.rate)
}

class LoanOperation(idgen: () => UUID) {
  private val loanRequests = new mutable.HashMap[UUID,LoanRequest]
  private val loanOffers = new mutable.HashMap[UUID,LoanOffer]
  private var existingOffers = Seq.empty[Offer]

  private val lock = new ReentrantLock()

  def createLoanRequest(amount: BigInt, days: BigInt): UUID = {
    require(amount > 0, "amount should be > 0")
    require(days > 0, "days should be > 0")

    val request = LoanRequest(idgen(), amount, days)
    loanRequests += request.id -> request
    calculate(amount).foreach { case CurrentOffer(a, r) => createLoanOffer(request.id, a, r) }
    request.id
  }

  def loanRequestsSize: Int = loanRequests.size

  def addOffer(amount: BigInt, rate: BigDecimal): UUID = {
    require(amount > 0, "amount should be > 0")
    require(rate > 0, "rate should be > 0")

    val offer = Offer(idgen(), amount, rate)
    lock.lock()
    existingOffers = offer +: existingOffers
    lock.unlock()
    offer.id
  }

  def offersSize: Int = existingOffers.size

  def createLoanOffer(loanRequestId: UUID, amount: BigInt, rate: BigDecimal): UUID = {
    require(amount > 0, "amount should be > 0")
    require(rate > 0, "rate should be > 0")

    val loanOffer = LoanOffer(idgen(), loanRequestId, amount, rate)
    loanOffers += loanRequestId -> loanOffer
    loanOffer.id
  }

  def loanOffersSize: Int = loanOffers.size

  def currentOffer(loanRequestId: UUID): Option[CurrentOffer] =
    loanOffers.get(loanRequestId).map(CurrentOffer(_))

  private def calculate(amount: BigInt): Option[CurrentOffer] = {
    type `borrowed,rate` = (BigInt,BigDecimal)

    val selectedOffers =
      existingOffers
        .sortBy(_.rate)
        .foldLeft((amount,Seq.empty[`borrowed,rate`])) { (a, offer) =>
          val (remainingToBorrow: BigInt, seq) = a
          if (remainingToBorrow == 0) a
          else {
            val borrowFromOffer = if (remainingToBorrow > offer.amount) offer.amount else remainingToBorrow
            (remainingToBorrow-borrowFromOffer, seq :+ (borrowFromOffer, offer.rate))
          }
        }._2

    val amountOffered = selectedOffers.map(_._1).sum

    // A = P(1 + rt)    P amount (110), r percent per year (0.05), t years (2)    A = 110(1 + (0.05 × 2)) = 121
    val amountWithInterest = selectedOffers.map { case (borrowed, r) => BigDecimal(borrowed)*(r/100+1) }.sum

    if (amountOffered > 0) {
      val combinedRate = amountWithInterest * 100 / BigDecimal(amountOffered) - 100
      Some(CurrentOffer(amountOffered, combinedRate))
    } else None

  }
}
