import de.heikoseeberger.sbtheader.HeaderPlugin.autoImport._
import de.heikoseeberger.sbtheader.CommentStyle.TwirlStyleBlockComment
import play.twirl.sbt.Import.TwirlKeys

headerLicense := Some(HeaderLicense.ALv2("2015", "Heiko Seeberger"))
headerMappings := Map("html" -> TwirlStyleBlockComment)

unmanagedSources.in(Compile, headerCreate) ++= sources.in(Compile, TwirlKeys.compileTemplates).value

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
          |  expected: $expectedPath
          |$expected
          |
          |  actual: $actualPath
          |$actual
          |""".stripMargin)
  }
}
