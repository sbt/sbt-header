organizationName := "Heiko Seeberger"
startYear        := Some(2015)
headerEndYear    := Some(2022)
licenses         := List(("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.txt")))

val checkFileContents = taskKey[Unit]("Verify file contents match expected contents")

checkFileContents := {
  checkFile("HasNoHeader.scala")

  def checkFile(name: String) = {
    val actualPath   = (scalaSource.in(Compile).value / name).toString
    val expectedPath = (resourceDirectory.in(Compile).value / s"${name}_expected").toString

    val actual   = scala.io.Source.fromFile(actualPath).mkString
    val expected = scala.io.Source.fromFile(expectedPath).mkString

    if (actual != expected) sys.error(s"""|Actual file contents do not match expected file contents!
          |  actual: $actualPath
          |$actual
          |
          |  expected: $expectedPath
          |$expected
          |""".stripMargin)
  }
}
