headerLicense := Some(HeaderLicense.ALv2("2015", "Heiko Seeberger"))

excludeFilter.in(unmanagedSources.in(headerCreate)) := HiddenFileFilter || "*Excluded.scala"

val checkFileContents = taskKey[Unit]("Verify file contents match expected contents")
checkFileContents := {

  val includeFiles = Seq(
    (scalaSource.in(Compile).value / "Included.scala").toString,
    (scalaSource.in(Compile).value / "de/heikoseeberger/allincluded/Included.scala").toString,
    (scalaSource.in(Compile).value / "de/heikoseeberger/mixed/Included.scala").toString
  )
  val excludeFiles = Seq(
    (scalaSource.in(Compile).value / "Excluded.scala").toString,
    (scalaSource.in(Compile).value / "de/heikoseeberger/allexcluded/Excluded.scala").toString,
    (scalaSource.in(Compile).value / "de/heikoseeberger/mixed/Excluded.scala").toString
  )

  val expectedExcludedFile = (resourceDirectory.in(Compile).value / "Excluded.scala_expected").toString
  val expectedIncludedFile = (resourceDirectory.in(Compile).value / "Included.scala_expected").toString

  checkFiles(excludeFiles, expectedExcludedFile)
  checkFiles(includeFiles, expectedIncludedFile)

  def checkFiles(filePaths: Seq[String], expectedPath: String) = {
    val expected = scala.io.Source.fromFile(expectedPath).mkString

    filePaths.foreach { actualPath =>
      val actual = scala.io.Source.fromFile(actualPath).mkString
  
      if (actual != expected) sys.error(
        s"""|Actual file contents do not match expected file contents!
            |  actual:   $actualPath
            |  expected: $expectedPath
            |""".stripMargin)
    }
  }

}
