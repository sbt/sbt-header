// *****************************************************************************
// Build settings
// *****************************************************************************

val scala212 = "2.12.20"
val scala3   = "3.7.3"

inThisBuild(
  Seq(
    crossScalaVersions := List(scala212, scala3),
    organization       := "com.github.sbt",
    organizationName   := "sbt plugins contributors",
    startYear          := Some(2015),
    licenses += License.Apache2,
    homepage := Some(url("https://github.com/sbt/sbt-header")),
    scmInfo  := Some(
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
    scalacOptions ++= List(
      "-unchecked",
      "-deprecation",
      "-encoding",
      "UTF-8",
    ),
    scalacOptions ++= {
      scalaBinaryVersion.value match {
        case "2.12" =>
          List("-Ywarn-unused:imports", "-Xsource:3")
        case _ => Nil
      }
    },
    scalafmtOnCompile := {
      scalaBinaryVersion.value match {
        case "2.12" => true
        case _      => false
      }
    },
    dynverSeparator := "_", // the default `+` is not compatible with docker tags
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
        "org.scalatest" %% "scalatest-wordspec"       % "3.2.19" % Test,
        "org.scalatest" %% "scalatest-shouldmatchers" % "3.2.19" % Test
      ),
      scriptedLaunchOpts ++= Seq(
        "-Xmx1024M",
        s"-Dplugin.version=${version.value}",
      ),
      (pluginCrossBuild / sbtVersion) := {
        scalaBinaryVersion.value match {
          case "2.12" => "1.9.9"
          case _      => "2.0.0-RC6"
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
