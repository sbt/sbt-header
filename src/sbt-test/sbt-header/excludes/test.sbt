headerLicense := Some(HeaderLicense.ALv2("2015", "Heiko Seeberger"))

excludeFilter.in(headerSources) := HiddenFileFilter || "*Excluded.scala"

val checkFiles = taskKey[Unit]("Verify files match expected files")
checkFiles := {

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

  // Check list of source files to process

  val nonHeaderCreateSources = unmanagedSources.in(Compile).value
  val headerCreateSources = headerSources.in(Compile).value

  def assertPathSetEquality(a: Seq[File], b: Seq[File], failureMessage: String): Unit = {
    val aSet = a.map(_.getCanonicalPath).toSet
    val bSet = b.map(_.getCanonicalPath).toSet
    if (aSet != bSet) {
      val abDiff = a diff b
      val baDiff = b diff a
      sys.error(
        s"""|$failureMessage.
            |  actual: $aSet
            |  expected: $bSet
            |  actual - expected: $abDiff
            |  expected - actual: $baDiff
            |""".stripMargin)

    }
  }

  assertPathSetEquality(
    nonHeaderCreateSources,
    (includeFiles ++ excludeFiles).map(new File(_)),
    "Expected source files for other (non-headerCreate) tasks to match include and exclude files.")
  assertPathSetEquality(
    headerCreateSources,
    includeFiles.map(new File(_)),
    "Expected source files for headerCreate task to match include files only.")

  // Check contents of files

  val expectedExcludedFile = (resourceDirectory.in(Compile).value / "Excluded.scala_expected").toString
  val expectedIncludedFile = (resourceDirectory.in(Compile).value / "Included.scala_expected").toString

  checkFileContents(excludeFiles, expectedExcludedFile)
  checkFileContents(includeFiles, expectedIncludedFile)

  def checkFileContents(filePaths: Seq[String], expectedPath: String) = {
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
