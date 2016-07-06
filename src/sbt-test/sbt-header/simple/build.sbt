import de.heikoseeberger.sbtheader.license.Apache2_0

headers := Map(
  "scala" -> Apache2_0("2015", "Heiko Seeberger")
)

TaskKey[Unit]("check") <<= scalaSource in Compile map { (scalaSource) =>

  val actualPath = (scalaSource / "Main.scala").toString
  val expectedPath = (scalaSource / "Main.scala_expected").toString

  val actual = scala.io.Source.fromFile(actualPath).mkString
  val expected = scala.io.Source.fromFile(expectedPath).mkString

  if (actual != expected) sys.error(
    s"""|Actual file contents do not match expected file contents!
        |  actual:   $actualPath
        |  expected: $expectedPath
        |""".stripMargin)
  ()
}
