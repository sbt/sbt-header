lazy val sbtHeader = project
  .in(file("."))
  .enablePlugins(AutomateHeaderPlugin, GitVersioning)

name := "sbt-header"

libraryDependencies ++= List(
  Library.scalaTest % "test"
)

sbtPlugin := true

initialCommands := """|import de.heikoseeberger.sbtheader._""".stripMargin
