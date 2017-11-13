headerLicense := Some(HeaderLicense.ALv2("2015", "Heiko Seeberger"))

headerMappings := Map(HeaderFileType.xml -> HeaderCommentStyle.xmlStyleBlockComment)

val checkFileContents = taskKey[Unit]("Verify file contents match expected contents")

checkFileContents := {
  checkFile("order-header-xmldeclaration.xml")
  checkFile("order-header-no-xmldeclaration.xml")
  checkFile("order-no-header-xmldeclaration.xml")
  checkFile("order-no-header-no-xmldeclaration.xml")

  def checkFile(name: String) = {
    val actualPath = (resourceDirectory.in(Compile).value / name).toString
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
          |
          |""".stripMargin)
  }
}

