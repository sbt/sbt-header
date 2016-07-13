import de.heikoseeberger.sbtheader.license.Apache2_0

headers := Map(
  "scala" -> Apache2_0("2015", "Heiko Seeberger")
)

val checkFileContents = taskKey[Unit]("Verify file contents match expected contents")
checkFileContents := {
  val actualPath = (scalaSource.in(Compile).value / "Main.scala").toString
  val expectedPath = (resourceDirectory.in(Compile).value / "Main.scala_expected").toString

  val actual = scala.io.Source.fromFile(actualPath).mkString
  val expected = scala.io.Source.fromFile(expectedPath).mkString

  if (actual != expected) sys.error(
    s"""|Actual file contents do not match expected file contents!
        |  actual:   $actualPath
        |  expected: $expectedPath
        |""".stripMargin)
}
