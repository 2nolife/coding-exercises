package com.sky.detector

import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, WordSpec, Suite}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.mockito.ArgumentCaptor

/** HackerDetector BDD spec */
@RunWith(classOf[JUnitRunner])
class HackerDetectorImplSpec extends Suite with WordSpec with BeforeAndAfterAll with BeforeAndAfter {

  var detector: HackerDetector = _

  before {
    detector = new HackerDetectorImpl
  }

  val epoch3pm = 1390834800

  /** First we create a skeleton of HackerDetectorImpl and then write some tests */

  "parseLine" should {

    "return None on empty string" in {
      expect(None) { detector.parseLine("") }
    }

    "return None on successful login" in {
      expect(None) { detector.parseLine(s"80.238.9.179,$epoch3pm,SIGNIN_SUCCESS,Dave.Branning") }
    }

    "return None on first failed login" in {
      expect(None) { detector.parseLine(s"80.238.9.179,$epoch3pm,SIGNIN_FAILURE,Dave.Branning") }
    }

    "return None on 3 failed login within 5 minutes" in {
      expect(None) { detector.parseLine(s"80.238.9.179,$epoch3pm,SIGNIN_FAILURE,Dave.Branning") }
      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+120},SIGNIN_FAILURE,Dave.Branning") }
      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+180},SIGNIN_FAILURE,Dave.Branning") }
    }

    "return IP on 5 failed login within 5 minutes" in {
      expect(None) { detector.parseLine(s"80.238.9.179,$epoch3pm,SIGNIN_FAILURE,Dave.Branning") } //15:00:00
      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+20},SIGNIN_FAILURE,Dave.Branning") }
      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+30},SIGNIN_FAILURE,Dave.Branning") }
      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+40},SIGNIN_FAILURE,Dave.Branning") }
      expect(Some("80.238.9.179")) { detector.parseLine(s"80.238.9.179,${epoch3pm+50},SIGNIN_FAILURE,Dave.Branning") } //15:00:50
    }

    "continue to return IP on more than 5 failed login within 5 minutes" in {
      expect(None) { detector.parseLine(s"80.238.9.179,$epoch3pm,SIGNIN_FAILURE,Dave.Branning") } //15:00:00
      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+20},SIGNIN_FAILURE,Dave.Branning") }
      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+30},SIGNIN_FAILURE,Dave.Branning") }
      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+40},SIGNIN_FAILURE,Dave.Branning") }
      expect(Some("80.238.9.179")) { detector.parseLine(s"80.238.9.179,${epoch3pm+50},SIGNIN_FAILURE,Dave.Branning") } //15:00:50
      expect(Some("80.238.9.179")) { detector.parseLine(s"80.238.9.179,${epoch3pm+60},SIGNIN_FAILURE,Dave.Branning") } //15:01:00
    }

    "return IP on 5 failed login within 5 minutes (interval borderline check)" in {
      expect(None) { detector.parseLine(s"80.238.9.179,$epoch3pm,SIGNIN_FAILURE,Dave.Branning") } //15:00:00
      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+20},SIGNIN_FAILURE,Dave.Branning") }
      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+30},SIGNIN_FAILURE,Dave.Branning") }
      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+40},SIGNIN_FAILURE,Dave.Branning") }
      expect(Some("80.238.9.179")) { detector.parseLine(s"80.238.9.179,${epoch3pm+60*5},SIGNIN_FAILURE,Dave.Branning") } //15:05:00
    }

    "not trigger alert on failed login outside of 5 minutes window" in {
      expect(None) { detector.parseLine(s"80.238.9.179,$epoch3pm,SIGNIN_FAILURE,Dave.Branning") } //15:00:00
      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+20},SIGNIN_FAILURE,Dave.Branning") }
      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+30},SIGNIN_FAILURE,Dave.Branning") }
      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+40},SIGNIN_FAILURE,Dave.Branning") }

      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+60*6},SIGNIN_FAILURE,Dave.Branning") } //6 minute delay, 15:06:00
      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+60*12},SIGNIN_FAILURE,Dave.Branning") } //6 minute delay. 15:12:00
      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+60*18},SIGNIN_FAILURE,Dave.Branning") } //6 minute delay. 15:18:00
    }

    "reset on successful login after a series of failed login" in {
      expect(None) { detector.parseLine(s"80.238.9.179,$epoch3pm,SIGNIN_FAILURE,Dave.Branning") } //15:00:00
      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+20},SIGNIN_FAILURE,Dave.Branning") }
      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+30},SIGNIN_FAILURE,Dave.Branning") }
      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+40},SIGNIN_FAILURE,Dave.Branning") }
      expect(Some("80.238.9.179")) { detector.parseLine(s"80.238.9.179,${epoch3pm+50},SIGNIN_FAILURE,Dave.Branning") } //alert 15:00:50

      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+60},SIGNIN_SUCCESS,Dave.Branning") } //start over 15:01:00
      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+70},SIGNIN_FAILURE,Dave.Branning") }
      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+80},SIGNIN_FAILURE,Dave.Branning") }
      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+90},SIGNIN_FAILURE,Dave.Branning") }
      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+100},SIGNIN_FAILURE,Dave.Branning") }
      expect(Some("80.238.9.179")) { detector.parseLine(s"80.238.9.179,${epoch3pm+120},SIGNIN_FAILURE,Dave.Branning") } //alert 15:02:00
    }

    "trigger alert for the correct IP (mixed users x3)" in {
      expect(None) { detector.parseLine(s"80.238.9.179,$epoch3pm,SIGNIN_FAILURE,Dave.Branning") } //15:00:00
      expect(None) { detector.parseLine(s"80.238.9.175,$epoch3pm,SIGNIN_FAILURE,Dave.Dromgold") } //15:00:00
      expect(None) { detector.parseLine(s"80.238.9.173,$epoch3pm,SIGNIN_FAILURE,Dave.Goldberg") } //15:00:00

      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+20},SIGNIN_FAILURE,Dave.Branning") }
      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+30},SIGNIN_FAILURE,Dave.Branning") }
      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+40},SIGNIN_FAILURE,Dave.Branning") }

      expect(None) { detector.parseLine(s"80.238.9.175,${epoch3pm+50},SIGNIN_FAILURE,Dave.Branning") }
      expect(None) { detector.parseLine(s"80.238.9.175,${epoch3pm+60},SIGNIN_FAILURE,Dave.Dromgold") }
      expect(None) { detector.parseLine(s"80.238.9.175,${epoch3pm+70},SIGNIN_FAILURE,Dave.Dromgold") }

      expect(None) { detector.parseLine(s"80.238.9.173,${epoch3pm+80},SIGNIN_FAILURE,Dave.Goldberg") }
      expect(None) { detector.parseLine(s"80.238.9.173,${epoch3pm+90},SIGNIN_FAILURE,Dave.Goldberg") }

      expect(Some("80.238.9.179")) { detector.parseLine(s"80.238.9.179,${epoch3pm+120},SIGNIN_FAILURE,Dave.Branning") }
      expect(Some("80.238.9.175")) { detector.parseLine(s"80.238.9.175,${epoch3pm+130},SIGNIN_FAILURE,Dave.Dromgold") }
      expect(Some("80.238.9.179")) { detector.parseLine(s"80.238.9.179,${epoch3pm+140},SIGNIN_FAILURE,Dave.Branning") }

      expect(None) { detector.parseLine(s"80.238.9.173,${epoch3pm+150},SIGNIN_FAILURE,Dave.Goldberg") }
      expect(Some("80.238.9.173")) { detector.parseLine(s"80.238.9.173,${epoch3pm+160},SIGNIN_FAILURE,Dave.Goldberg") }
    }

    "trigger alerts on failed login within 5 minutes window and do not trigger outside of it " in {
      expect(None) { detector.parseLine(s"80.238.9.179,$epoch3pm,SIGNIN_FAILURE,Dave.Branning") } // a 15:00:00
      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+60*1},SIGNIN_FAILURE,Dave.Branning") } // b
      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+60*2},SIGNIN_FAILURE,Dave.Branning") } // c
      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+60*3},SIGNIN_FAILURE,Dave.Branning") } // d
      expect(Some("80.238.9.179")) { detector.parseLine(s"80.238.9.179,${epoch3pm+60*4},SIGNIN_FAILURE,Dave.Branning") } // e first alert 15:04:00 (failed: a,b,c,d,e)

      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+60*8},SIGNIN_FAILURE,Dave.Branning") } // i (4 minute delay) 15:08:00 (failed: d,e,i)

      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+60*9},SIGNIN_FAILURE,Dave.Branning") } // j (failed: e,i,j)
      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+60*10},SIGNIN_FAILURE,Dave.Branning") } // h (failed: i,j,h)
      expect(None) { detector.parseLine(s"80.238.9.179,${epoch3pm+60*11},SIGNIN_FAILURE,Dave.Branning") } // g (failed: i,j,h,g)
      expect(Some("80.238.9.179")) { detector.parseLine(s"80.238.9.179,${epoch3pm+60*12},SIGNIN_FAILURE,Dave.Branning") } // f alert (failed: i,j,h,g,f)
      expect(Some("80.238.9.179")) { detector.parseLine(s"80.238.9.179,${epoch3pm+60*13},SIGNIN_FAILURE,Dave.Branning") } // x alert (failed: i,j,h,g,f,x)
    }

  }

  /** Tests are in place and now we ready to implement the HackerDetectorImpl */

  /** Implementing the HackerDetectorImpl */

  /** What you know? All tests passed!
      Adding comments, some readability, and done. */

}

/** Alerter BDD spec */
@RunWith(classOf[JUnitRunner])
class AlerterSpec extends Suite with MockitoSugar with WordSpec with BeforeAndAfterAll with BeforeAndAfter {

  var alerter: Alerter = _
  var failedStore: FailedStore = _

  before {
    failedStore = mock[FailedStore]
    alerter = new Alerter(failedStore)
  }

  "isHacker" should {

    "not alert on no failed records" in {
      val e = LogEntry("80.238.9.179",133612947,"SIGNIN_FAILURE","Dave.Branning")
      when(failedStore.processEntry(e)).thenReturn(Nil)
      expect(false) { alerter.isHacker(e) }
    }

    "not alert on less than 5 failed login within 5 minutes" in {
      val e = LogEntry("80.238.9.179",133612947,"SIGNIN_FAILURE","Dave.Branning")
      when(failedStore.processEntry(e)).thenReturn(e :: e :: e :: Nil)
      expect(false) { alerter.isHacker(e) }
    }

    "alert on 5 or more failed login within 5 minutes" in {
      val e = LogEntry("80.238.9.179",133612947,"SIGNIN_FAILURE","Dave.Branning")
      when(failedStore.processEntry(e)).thenReturn(e :: e :: e :: e :: e :: Nil)
      expect(true) { alerter.isHacker(e) }
    }

  }

}

/** Alerter BDD spec */
@RunWith(classOf[JUnitRunner])
class FailedStoreSpec extends Suite with MockitoSugar with WordSpec with BeforeAndAfterAll with BeforeAndAfter {

  var failedStore: FailedStore = _

  before {
    failedStore = new FailedStore {
      override val oldInterval = 1 // 1 sec delay
    }
  }

  //Helper method to get log entries from the failed map
  def fmList = failedStore.failedMap.get("80.238.9.179").get._1

  "prune" should {
    "remove old IP records" in {
      val epoch = System.currentTimeMillis/1000L
      val e1 = LogEntry("80.238.9.179",epoch,"SIGNIN_FAILURE","Dave.Branning")
      val e2 = LogEntry("80.238.9.179",epoch,"SIGNIN_FAILURE","Dave.Branning")
      val e3 = LogEntry("80.238.9.179",epoch,"SIGNIN_FAILURE","Dave.Branning")

      expect(List(e1)) { failedStore.processEntry(e1) }
      expect(List(e1)) { fmList }
      expect(List(e2,e1)) { failedStore.processEntry(e2) }
      expect(List(e2,e1)) { fmList }

      Thread.sleep(1010)
      expect(List(e3)) { failedStore.processEntry(e3) }
      expect(List(e3)) { fmList }
    }
  }

  "processEntry" should {
    "remove old failed attempts" in {
      val epoch = System.currentTimeMillis/1000L
      val e1 = LogEntry("80.238.9.179",epoch,"SIGNIN_FAILURE","Dave.Branning")
      val e2 = LogEntry("80.238.9.179",epoch,"SIGNIN_FAILURE","Dave.Branning")
      val e3 = LogEntry("80.238.9.179",epoch+2,"SIGNIN_FAILURE","Dave.Branning")

      expect(List(e1)) { failedStore.processEntry(e1) }
      expect(List(e1)) { fmList }
      expect(List(e2,e1)) { failedStore.processEntry(e2) }
      expect(List(e2,e1)) { fmList }

      expect(List(e3)) { failedStore.processEntry(e3) }
      expect(List(e3)) { fmList }
    }
  }

}
