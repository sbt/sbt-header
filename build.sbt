// *****************************************************************************
// Build settings
// *****************************************************************************

inThisBuild(
  Seq(
    organization     := "de.heikoseeberger",
    organizationName := "Heiko Seeberger",
    startYear        := Some(2015),
    licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0")),
    homepage := Some(url("https://github.com/sbt/sbt-header")),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/sbt/sbt-header"),
        "git@github.com:sbt/sbt-header.git"
      )
    ),
    developers := List(
      Developer(
        "hseeberger",
        "Heiko Seeberger",
        "mail@heikoseeberger.de",
        url("https://github.com/hseeberger")
      )
    ),
    // scalaVersion defined by sbt
    scalacOptions ++= Seq(
      "-unchecked",
      "-deprecation",
      "-language:_",
      "-encoding",
      "UTF-8",
      "-Ywarn-unused:imports",
    ),
    scalafmtOnCompile := true,
    dynverSeparator   := "_", // the default `+` is not compatible with docker tags
  )
)

// *****************************************************************************
// Projects
// *****************************************************************************

lazy val `sbt-header` =
  project
    .in(file("."))
    .enablePlugins(AutomateHeaderPlugin, SbtPlugin)
    .settings(commonSettings)
    .settings(
      libraryDependencies ++= Seq(
        library.scalaTest % Test,
      ),
      scriptedLaunchOpts ++= Seq(
        "-Xmx1024M",
        s"-Dplugin.version=${version.value}",
      ),
    )

// *****************************************************************************
// Project settings
// *****************************************************************************

lazy val commonSettings =
  Seq(
    // Also (automatically) format build definition together with sources
    Compile / scalafmt := {
      val _ = (Compile / scalafmtSbt).value
      (Compile / scalafmt).value
    },
  )

// *****************************************************************************
// Library dependencies
// *****************************************************************************

lazy val library =
  new {
    object Version {
      val scalaTest = "3.2.14"
    }
    val scalaTest = "org.scalatest" %% "scalatest" % Version.scalaTest
  }
