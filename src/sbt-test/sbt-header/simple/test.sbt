headerLicense := Some(HeaderLicense.ALv2("2015", "Heiko Seeberger"))

// A file named exactly "routes" will NOT be taken into account even if the header extension is "routes"
// A hidden file .routes must be treated with file name ".routes" and without extension
// A hidden file .some.routes must be treated with extension "*.routes"
headerMappings := headerMappings.value + (HeaderFileType(
  "routes"
) -> HeaderCommentStyle.cppStyleLineComment)
// Do not exclude hidden files (.filename)
unmanagedResources / excludeFilter := (_ => false)

val checkFileContents = taskKey[Unit]("Verify file contents match expected contents")

checkFileContents := {
  checkFile("HasHeader.scala", (Compile / scalaSource).value)
  checkFile("HasNoHeader.scala", (Compile / scalaSource).value)
  checkFile("routes", (Compile / resourceDirectory).value)
  checkFile(
    ".routes",
    (Compile / resourceDirectory).value
  ) // does not have extension, file name is only ".routes"
  checkFile(
    ".some.routes",
    (Compile / resourceDirectory).value
  ) // hidden file with extension .routes

  def checkFile(name: String, sourceDir: sbt.File) = {
    val actualPath   = (sourceDir / name).toString
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
