import com.typesafe.sbt.SbtGit
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import de.heikoseeberger.sbtheader.SbtHeader
import de.heikoseeberger.sbtheader.license.Apache2_0
import sbt._
import sbt.Keys._
import scalariform.formatter.preferences._

object Build extends AutoPlugin {

  override def requires = plugins.JvmPlugin

  override def trigger = allRequirements

  override def projectSettings =
    // Core settings
    List(
      organization := "de.heikoseeberger",
      licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0")),
      // scalaVersion := Version.scala,
      // crossScalaVersions := List(scalaVersion.value),
      scalacOptions ++= List(
        "-unchecked",
        "-deprecation",
        "-language:_",
        "-target:jvm-1.7",
        "-encoding", "UTF-8"
      ),
      unmanagedSourceDirectories.in(Compile) := List(scalaSource.in(Compile).value),
      unmanagedSourceDirectories.in(Test) := List(scalaSource.in(Test).value),
      publishTo := Some(if (isSnapshot.value) Classpaths.sbtPluginSnapshots else Classpaths.sbtPluginReleases),
      publishMavenStyle := false
    ) ++
    // Scalariform settings
    SbtScalariform.scalariformSettings ++
    List(
      ScalariformKeys.preferences := ScalariformKeys.preferences.value
        .setPreference(AlignArguments, true)
        .setPreference(AlignParameters, true)
        .setPreference(AlignSingleLineCaseStatements, true)
        .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 100)
        .setPreference(DoubleIndentClassDeclaration, true)
    ) ++
    // Git settings
    SbtGit.versionWithGit ++
    List(
      SbtGit.git.baseVersion := "1.3.0"
    ) ++
    // Header settings
    SbtHeader.automate(Compile, Test) ++
    List(
      SbtHeader.autoImport.headers := Map(
        "scala" -> Apache2_0("2015", "Heiko Seeberger")
      )
    )
}
