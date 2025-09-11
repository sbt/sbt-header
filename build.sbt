// *****************************************************************************
// Build settings
// *****************************************************************************

inThisBuild(
  Seq(
    organization     := "com.github.sbt",
    organizationName := "sbt plugins contributors",
    startYear        := Some(2015),
    licenses += License.Apache2,
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
      headerLicense := Some(
        HeaderLicense.Custom(
          """|Copyright (c) 2015 - 2025, Heiko Seeberger
             |Copyright (c) 2025, sbt plugin contributors
             |
             |SPDX-License-Identifier: Apache-2.0
             |""".stripMargin
        )
      ),
      libraryDependencies ++= Seq(
        library.scalaTest % Test,
      ),
      scriptedLaunchOpts ++= Seq(
        "-Xmx1024M",
        s"-Dplugin.version=${version.value}",
      ),
      (pluginCrossBuild / sbtVersion) := {
        scalaBinaryVersion.value match {
          case "2.12" => "1.5.8"
          case _      => "2.0.0-RC4"
        }
      },
      scriptedSbt := {
        scalaBinaryVersion.value match {
          case "2.12" => "1.11.6"
          case _      => (pluginCrossBuild / sbtVersion).value
        }
      },
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
