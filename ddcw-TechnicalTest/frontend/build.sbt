import play.sbt.PlayImport._
import sbt._

version := "1.0-SNAPSHOT"

lazy val compileDeps = Seq(
  ws,
  guice
)

def test(scope: String) = Seq(
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % scope,
  "org.scalatest" %% "scalatest" % "3.2.9" % scope,
  "org.scalamock" %% "scalamock" % "4.1.0" % scope
)

lazy val root = (project in file("."))
    .settings(name := "ddcw-technical-test-frontend",
      organization := "com.hmrc",
      scalaVersion := "2.12.13",
      libraryDependencies ++= compileDeps ++ test("test"),
      PlayKeys.playDefaultPort := 1234)
  .enablePlugins(PlayScala)
