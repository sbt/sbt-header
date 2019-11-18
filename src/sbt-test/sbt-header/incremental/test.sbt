import java.nio.file.Files

headerLicense := Some(HeaderLicense.ALv2("2015", "Heiko Seeberger"))

val checkFileContents = taskKey[Unit]("Verify file contents match expected contents")
val stripHeader = taskKey[Unit]("Strip headers")
val removeFile = taskKey[Unit]("Remove one file")

stripHeader := {
  import java.nio.file.Files
  stripHeader("HasHeader.scala")

  def stripHeader(name: String) = {
    val actualPath = (scalaSource.in(Compile).value / name).toString
    val headerDropped = (resourceDirectory.in(Compile).value / s"${name}_headerdropped").toString

    Files.delete(file(actualPath).toPath)
    Files.copy(file(headerDropped).toPath, file(actualPath).toPath)
  }
}

checkFileContents := {
  checkFile("HasHeader.scala")
  checkFile("HasNoHeader.scala")

  def checkFile(name: String) = {
    val actualPath = (scalaSource.in(Compile).value / name).toString
    val expectedPath = (resourceDirectory.in(Compile).value / s"${name}_expected").toString

    val actual = scala.io.Source.fromFile(actualPath).mkString
    val expected = scala.io.Source.fromFile(expectedPath).mkString

    if (actual != expected) sys.error(
      s"""|Actual file contents do not match expected file contents!
          |  actual: $actualPath
          |$actual
          |
          |  expected: $expectedPath
          |$expected
          |""".stripMargin)
  }
}

removeFile := {
  val actualPath = (scalaSource.in(Compile).value / "HasNoHeader.scala")
  Files.delete(actualPath.toPath)
}

