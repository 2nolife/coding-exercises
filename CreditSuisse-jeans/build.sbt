name := "CreditSuisse-jeans"

Common.settings

libraryDependencies ++= Dependencies.Jeans

mainClass in (Compile, run) := Some("creditsuisse.jeans.ms.orders.start")

lazy val root = (project in file(".")).
  configs(IntegrationTest).
  settings(Defaults.itSettings: _*)

parallelExecution in Test := false
parallelExecution in IntegrationTest := false