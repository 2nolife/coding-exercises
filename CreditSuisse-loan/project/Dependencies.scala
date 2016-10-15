import sbt._

object Version {
  val akka      = "2.4.9"
  val logback   = "1.1.3"
  val scala     = "2.11.8"
  val scalaTest = "2.2.5"
  val mockito   = "1.10.19"
  val httpclient = "4.5.2"
}

object Library {
  val akkaActor       = "com.typesafe.akka" %% "akka-actor"                        % Version.akka
  val akkaHttp        = "com.typesafe.akka" %% "akka-http-experimental"            % Version.akka
  val akkaSprayJson   = "com.typesafe.akka" %% "akka-http-spray-json-experimental" % Version.akka
  val akkaSlf4j       = "com.typesafe.akka" %% "akka-slf4j"                        % Version.akka
  val logbackClassic  = "ch.qos.logback"    %  "logback-classic"        % Version.logback
  val scalaTest       = "org.scalatest"     %% "scalatest"              % Version.scalaTest
  val mockito         = "org.mockito"       %  "mockito-core"           % Version.mockito
  val httpclient      = "org.apache.httpcomponents" % "httpclient" % Version.httpclient
}

object Dependencies {

  import Library._

  val SlotsBooker = List(
    akkaActor,
    akkaHttp,
    akkaSprayJson,
    akkaSlf4j,
    logbackClassic,
    httpclient,
    scalaTest   % "test,it",
    mockito     % "test,it"
  )
}
