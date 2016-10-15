name := "creditsuisse-loan"

Common.settings

libraryDependencies ++= Dependencies.SlotsBooker

mainClass in (Compile, run) := Some("creditsuisse_loan.rest.WebServer")

lazy val root = (project in file(".")).
  configs(IntegrationTest).
  settings(Defaults.itSettings: _*)

parallelExecution in Test := false
parallelExecution in IntegrationTest := false