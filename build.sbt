lazy val sbtHeader = project.in(file("."))

name := "sbt-header"

libraryDependencies ++= List(
)

initialCommands := """|import de.heikoseeberger.sbtheader._""".stripMargin
