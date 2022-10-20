headerLicense := Some(HeaderLicense.ALv2("2015", "Heiko Seeberger"))

// Files named exactly "routes" (with no extension)
headerMappings := headerMappings.value + (HeaderFileType("", None, "routes") -> HeaderCommentStyle.hashLineComment)
// Files named exactly "more.routes"
headerMappings := headerMappings.value + (HeaderFileType("routes", None, "more") -> HeaderCommentStyle.cStyleBlockComment)
// Hidden files named exactly ".routes"
headerMappings := headerMappings.value + (HeaderFileType("", None, ".routes") -> HeaderCommentStyle.cppStyleLineComment)

// Do not exclude hidden files (.filename)
unmanagedResources / excludeFilter := (_ => false)


val checkFileContents = taskKey[Unit]("Verify file contents match expected contents")

checkFileContents := {
  checkFile("routes")
  checkFile("more.routes")
  checkFile(".routes")

  def checkFile(name: String) = {
    val actualPath   = (resourceDirectory.in(Compile).value / name).toString
    val expectedPath = (resourceDirectory.in(Compile).value / s"${name}_expected").toString

    val actual   = scala.io.Source.fromFile(actualPath).mkString
    val expected = scala.io.Source.fromFile(expectedPath).mkString

    if (actual != expected) sys.error(s"""|Actual file contents do not match expected file contents!
                                          |  actual: $actualPath
                                          |$actual
                                          |
                                          |  expected: $expectedPath
                                          |$expected
                                          |
                                          |""".stripMargin)
  }
}
