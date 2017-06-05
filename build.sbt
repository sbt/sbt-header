// *****************************************************************************
// Projects
// *****************************************************************************

lazy val `sbt-header` =
  project
    .in(file("."))
    .enablePlugins(AutomateHeaderPlugin, GitVersioning)
    .settings(settings)
    .settings(
      libraryDependencies ++= Seq(
        library.scalaTest % Test
      )
    )

// *****************************************************************************
// Library dependencies
// *****************************************************************************

lazy val library =
  new {
    object Version {
      val scalaTest = "3.0.3"
    }
    val scalaTest = "org.scalatest" %% "scalatest" % Version.scalaTest
  }

// *****************************************************************************
// Settings
// *****************************************************************************        |

lazy val settings =
  commonSettings ++
  gitSettings ++
  pluginSettings

lazy val commonSettings =
  Seq(
    organization := "de.heikoseeberger",
    organizationName := "Heiko Seeberger",
    startYear := Some(2015),
    licenses += ("Apache-2.0",
                 url("http://www.apache.org/licenses/LICENSE-2.0")),
    mappings.in(Compile, packageBin) += baseDirectory.in(ThisBuild).value / "LICENSE" -> "LICENSE",
    scalacOptions ++= Seq(
      "-unchecked",
      "-deprecation",
      "-language:_",
      "-target:jvm-1.7",
      "-encoding", "UTF-8"
    ),
    unmanagedSourceDirectories.in(Compile) := Seq(scalaSource.in(Compile).value),
    unmanagedSourceDirectories.in(Test) := Seq(scalaSource.in(Test).value)
)

lazy val gitSettings =
  Seq(
    git.useGitDescribe := true
  )

lazy val pluginSettings =
  scriptedSettings ++
  Seq(
    sbtPlugin := true,
    publishMavenStyle := false,
    scriptedLaunchOpts ++= Seq("-Xmx1024M", s"-Dplugin.version=${version.value}"),
    scriptedBufferLog := false
  )
