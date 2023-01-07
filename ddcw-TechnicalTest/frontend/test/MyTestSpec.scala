import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import org.scalatestplus.play.OneBrowserPerSuite
import org.scalatestplus.play.HtmlUnitFactory
import java.io.File
import scala.annotation.tailrec

class TestSpec extends PlaySpec  {

  "it" must {
    "count files in dir" in {
         countFiles("/vault/dev/exercises/ddcw-technical-test-frontend/test/resources") mustBe 8
         countFiles2("/vault/dev/exercises/ddcw-technical-test-frontend/test/resources") mustBe 8
    }

    "combine list into tuples" in {
      val members = List("Sam", "Thompson", "John", "Sweet")
      combineTokens(members) must contain allOf (
        ("Sam", "Thompson"), ("Sam", "John"), ("Sam", "Sweet"),
        ("Thompson", "John"), ("Thompson", "Sweet"),
        ("John", "Sweet")
      )
      combineTokens2(members, List(("Thompson", "Sweet"))) mustBe  List(("Sam", "John"))
    }
  }

  def countFiles(path: String): Int = {
    @tailrec def countFilesInDir(dirs: List[File], acc: Int = 0): Int = {
      dirs match {
        case Nil => acc
        case dir :: rest =>
          val fileCount = dir.listFiles.count(_.isFile)
          val moreDirs = dir.listFiles.filter(_.isDirectory).toList
          countFilesInDir(rest ::: moreDirs, acc + fileCount)
      }
    }
    countFilesInDir(new File(path) :: Nil)
  }

  def countFiles2(path: String): Int = {
    @tailrec def countFilesInDir(files: List[File], acc: Int = 0): Int = {
      files match {
        case Nil => acc
        case file :: rest =>
          if (file.isFile) countFilesInDir(rest, acc+1) else countFilesInDir(file.listFiles.toList ::: rest, acc)
      }
    }
    countFilesInDir(new File(path) :: Nil)
  }

  def combineTokens(members: List[String]): List[(String,String)] = {
    @tailrec def fn(xs: List[String], acc: List[(String,String)] = Nil): List[(String,String)] =
      xs match {
        case Nil | _ :: Nil => acc
        case m1 :: m2 :: rest => fn(m2 :: rest, (m2 :: rest).map(m => m1 -> m) ::: acc)
      }
    fn(members)
  }

  def combineTokens2(members: List[String], exclude: List[(String,String)]): List[(String,String)] = {
    @tailrec def fn(xs: List[String], acc: List[(String,String)] = Nil): List[(String,String)] =
      xs match {
        case Nil | _ :: Nil => acc
        case m1 :: m2 :: rest => fn(m2 :: rest, (m2 :: rest).map(m => m1 -> m) ::: acc)
      }
    fn(members).flatMap { case m @ (m1, m2) => if (exclude.exists { case (e1, e2) => (e1 :: e2 :: Nil).intersect(m1 :: m2 :: Nil).nonEmpty }) None else Some(m) }
  }




//  def myfunc(xs: Seq[Int]): Option[Int] = {
//    Option(xs).getOrElse(Seq.empty[Int]).sorted.reverse.lift(1)
//  }

//  def myfunc(xs: Seq[Int]): Option[Int] = {
//    val safeXs = Option(xs).getOrElse(Seq.empty[Int])
//    safeXs match {
//      case Nil => None
//      case _ => Option(safeXs.max).map(max => safeXs.filter(_ < max).max)
//    }
//  }

  def myfunc(numbers: Seq[Int]): Option[Int] = {
    @tailrec def m(xs: Seq[Int], curMax: Int, acc2: Int): Int = {
      xs match {
        case Nil => curMax
        case Seq(a) => if (curMax > a) curMax else a
        case a :: b :: tail => m(tail, a, b)
      }
    }

    @tailrec def f(xs: Seq[Int], max: Int, curMax: Int, acc2: Int): Int = {
      xs match {
        case Nil => curMax
        case Seq(a) => if (curMax > a && curMax != max) curMax else a
        case a :: b :: tail => f(tail, max, a, b)
      }
    }

    Option(numbers).getOrElse(Seq.empty[Int]) match {
      case Nil => None
      case Seq(a) => Some(a)
      case a :: b :: tail =>
        val max = m(tail, a, b)
        Some(f(tail, max, a, b))
    }
  }


  "it" must {
    "return second biggest number" in {
      myfunc(Seq.empty) mustBe None
      myfunc(3 :: 5 :: 14 :: 0 :: 8 :: Nil) mustBe Some(8)
      myfunc(14 :: Nil) mustBe Some(14)
      myfunc(14 :: 14 :: Nil) mustBe Some(14)
      myfunc(null) mustBe None
    }
  }


}