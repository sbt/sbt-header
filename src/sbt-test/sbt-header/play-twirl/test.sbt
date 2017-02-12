import de.heikoseeberger.sbtheader.license.Apache2_0

headers := Map(
  "html" -> Apache2_0("2015", "Heiko Seeberger")
)

val checkFileContents = taskKey[Unit]("Verify file contents match expected contents")
checkFileContents := {
  checkFile("views/index.scala.html")
  checkFile("views/main.scala.html")

  def checkFile(name: String) = {
    val actualPath = (scalaSource.in(Compile).value / name).toString
    val expectedPath = (scalaSource.in(Compile).value / (name + "_expected")).toString

    val actual = scala.io.Source.fromFile(actualPath).mkString
    val expected = scala.io.Source.fromFile(expectedPath).mkString

    if (actual != expected) sys.error(
      s"""|Actual file contents do not match expected file contents!
          |  actual:
          |$actual
          |
          |  expected:
          |$expected
          |""".stripMargin)
  }
}

