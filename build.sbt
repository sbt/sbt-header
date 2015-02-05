lazy val sbtHeader = project.in(file("."))

name := "sbt-header"

libraryDependencies ++= List(
  Library.scalaTest % "test"
)

sbtPlugin := true

initialCommands := """|import de.heikoseeberger.sbtheader._""".stripMargin
