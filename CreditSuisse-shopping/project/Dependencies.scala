import sbt._

object Version {
  val scala     = "2.11.7"
  val scalaTest = "2.2.5"
  val mockito   = "1.10.19"
}

object Library {
  val scalaTest       = "org.scalatest"     %% "scalatest"                     % Version.scalaTest
  val mockito         = "org.mockito"       %  "mockito-core"                  % Version.mockito
}

object Dependencies {

  import Library._

  val creditsuisse = List(
    scalaTest   % "test",
    mockito     % "test"
  )
}
