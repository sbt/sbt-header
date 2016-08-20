import de.heikoseeberger.sbtheader.license.Apache2_0

headers := Map(
  "scala" -> Apache2_0("2015", "Heiko Seeberger")
)

excludes := Seq(
  "src/main/scala/Excluded.scala",
  "src/main/scala/de/heikoseeberger/mixed/Excluded.scala",
  "src/main/scala/de/heikoseeberger/allexcluded/*.scala",
  "src/main/scala/de/heikoseeberger/allincluded/*.java"
)

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
