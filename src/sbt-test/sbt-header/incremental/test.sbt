import java.nio.file.Files

headerLicense := Some(HeaderLicense.ALv2("2015", "Heiko Seeberger"))

val checkFileContents = taskKey[Unit]("Verify file contents match expected contents")
val stripHeader       = taskKey[Unit]("Strip headers")
val removeFile        = taskKey[Unit]("Remove one file")

stripHeader := {
  import java.nio.file.Files
  stripHeader("HasHeader.scala")

  def stripHeader(name: String) = {
    val actualPath    = ((Compile / scalaSource).value / name).toString
    val headerDropped = ((Compile / resourceDirectory).value / s"${name}_headerdropped").toString

    Files.delete(file(actualPath).toPath)
    Files.copy(file(headerDropped).toPath, file(actualPath).toPath)
  }
}

checkFileContents := {
  checkFile("HasHeader.scala")
  checkFile("HasNoHeader.scala")

  def checkFile(name: String) = {
    val actualPath   = ((Compile / scalaSource).value / name).toString
    val expectedPath = ((Compile / resourceDirectory).value / s"${name}_expected").toString

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

removeFile := {
  val actualPath = (Compile / scalaSource).value / "HasNoHeader.scala"
  Files.delete(actualPath.toPath)
}
