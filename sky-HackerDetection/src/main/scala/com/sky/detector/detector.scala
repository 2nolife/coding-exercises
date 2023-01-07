package com.sky.detector

import scala.collection.mutable.{SynchronizedMap,HashMap}

/** Hacker detector interface */
trait HackerDetector {
  def parseLine(line: String): Option[String] //rule of thumb: avoid "null" we shall use Option instead
}

/** Hacker detector constants */
object HackerDetector {
  val LOGIN_SUCCESS = "SIGNIN_SUCCESS"
  val LOGIN_FAILURE = "SIGNIN_FAILURE"
}

/** Hacker detector implementation
  * (c) by S Abramov   20140117
  */
class HackerDetectorImpl extends HackerDetector {
  val alerter = new Alerter(new FailedStore)

  override def parseLine(line: String): Option[String] =
    line.split(",").toList match {
      case List(ip, date, action, username) if alerter.isHacker(LogEntry(ip, date.toLong, action, username)) => Some(ip)
      //case List(ip, date, action, username) if alerter.isHacker(LogEntry(ip, date.toLong, action, username)) => Some(ip)
      case _ => None
    }
}

/** This is a log entry */
case class LogEntry(ip: String, date: Long, action: String, username: String)

/** This class keeps all failed log entries per IP  */
class FailedStore {
  import HackerDetector._

  //keep log entries for the last 15 minutes and keep IP records accessed within 15 minutes
  val oldInterval = 15*60

  //map key is IP and value is a tuple of failed login entries and last accessed time
  val failedMap = new HashMap[String,(List[LogEntry],Long)] with SynchronizedMap[String,(List[LogEntry],Long)]

  def processEntry(logEntry: LogEntry): List[LogEntry] =
    logEntry.action match {
      case LOGIN_SUCCESS =>
        failedMap -= logEntry.ip
        Nil
      case LOGIN_FAILURE =>
        prune
        val entries = failedMap.get(logEntry.ip).getOrElse((Nil,System.currentTimeMillis))
        val xs = logEntry :: entries._1.takeWhile(_.date >= logEntry.date-oldInterval) //remove old entries
        failedMap += (logEntry.ip -> (xs,System.currentTimeMillis))
        xs
      case _ =>
        Nil
    }

  /** Remove old IP records */
  def prune {
    for ((ip, (_, lastAccessed)) <- failedMap)
      if (lastAccessed < System.currentTimeMillis-oldInterval*1000L) failedMap -= ip
  }
}

/** This class is smart enough to know when 5 or more failed attempts were made within 5 minutes */
class Alerter(failedStore: FailedStore) {
  val failedTimeInterval = 5*60
  val failedAttempts = 5

  def isHacker(logEntry: LogEntry): Boolean =
    failedStore.processEntry(logEntry) match {
      case Nil => false
      case entries => entries.takeWhile(_.date >= logEntry.date-failedTimeInterval).size >= failedAttempts
    }
}
