import com.typesafe.sbt.SbtGit._
import com.typesafe.sbt.SbtScalariform._
import sbt._
import sbt.Keys._
import sbtrelease.ReleasePlugin._
import scalariform.formatter.preferences._

object Build extends AutoPlugin {

  override def requires =
    plugins.JvmPlugin

  override def trigger =
    allRequirements

  override def projectSettings =
    scalariformSettings ++
    versionWithGit ++
    releaseSettings ++
    List(
      // Core settings
      organization := "de.heikoseeberger",
      //scalaVersion := Version.scala,
      //crossScalaVersions := List(scalaVersion.value),
      scalacOptions ++= List(
        "-unchecked",
        "-deprecation",
        "-language:_",
        "-target:jvm-1.7",
        "-encoding", "UTF-8"
      ),
      unmanagedSourceDirectories in Compile := List((scalaSource in Compile).value),
      unmanagedSourceDirectories in Test := List((scalaSource in Test).value),
      // Publish settings
      publishTo := Some(if (isSnapshot.value) Classpaths.sbtPluginSnapshots else Classpaths.sbtPluginReleases),
      publishMavenStyle := false,
      // Scalariform settings
      ScalariformKeys.preferences := ScalariformKeys.preferences.value
        .setPreference(AlignArguments, true)
        .setPreference(AlignParameters, true)
        .setPreference(AlignSingleLineCaseStatements, true)
        .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 100)
        .setPreference(DoubleIndentClassDeclaration, true),
      // Git settings
      git.baseVersion := "0.1.0",
      // Release settings
      ReleaseKeys.versionBump := sbtrelease.Version.Bump.Minor
    )
}
