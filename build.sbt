lazy val sbtHeader = project
  .in(file("."))
  .enablePlugins(AutomateHeaderPlugin, GitVersioning)

name := "sbt-header"

sbtPlugin := true

libraryDependencies ++= List(
  Library.scalaTest % "test"
)

initialCommands := """|import de.heikoseeberger.sbtheader._""".stripMargin
