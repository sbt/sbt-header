/*
 * Copyright (c) 2015 - 2025, Heiko Seeberger
 * Copyright (c) 2025, sbt plugin contributors
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package sbtheader

import sbtheader.CommentStyle.cStyleBlockComment
import java.nio.charset.StandardCharsets.UTF_8
import java.nio.file.Files
import sbt.{
  AutoPlugin,
  Compile,
  Configuration,
  File,
  Logger,
  ScopeFilter,
  Setting,
  SettingKey,
  TaskKey,
  Test,
  inAnyConfiguration,
  inConfig,
  settingKey,
  taskKey
}
import sbt.Defaults.collectFiles
import sbt.Keys._
import sbt.internal.util.MessageOnlyException
import sbt.plugins.JvmPlugin
import sbt.util.FileFunction
import scala.util.matching.Regex

object HeaderPlugin extends AutoPlugin {

  object autoImport {

    val HeaderLicense = License

    val HeaderLicenseStyle = LicenseStyle

    object HeaderPattern {

      def commentBetween(start: String, middle: String, end: String): Regex =
        raw"""(?s)($start(?!\$middle).*?$end(?:\n|\r|\r\n)+)(.*)""".r

      def commentStartingWith(start: String): Regex =
        raw"""(?s)((?:$start[^\n\r]*(?:\n|\r|\r\n))*(?:$start[^\n\r]*(?:(?:\n)+|(?:\r)+|(?:\r\n)+)+))(.*)""".r
    }

    val HeaderFileType = FileType

    val HeaderCommentStyle = CommentStyle

    val headerEndYear: SettingKey[Option[Int]] =
      settingKey(
        "The end of the range of years to specify in the header. Defaults to None (only the `startYear` is used)."
      )

    val headerLicense: SettingKey[Option[License]] =
      settingKey(
        "The license to apply to files; None by default (enabling auto detection from project settings)"
      )

    val headerLicenseFallback: SettingKey[Option[License]] =
      settingKey(
        "The license crated by auto detection from the project settings"
      )

    val headerLicenseStyle: SettingKey[LicenseStyle] =
      settingKey[LicenseStyle] {
        "The license style to be used. Can be `Detailed` or `SpdxSyntax`. Defaults to Detailed."
      }

    val headerMappings: SettingKey[Map[FileType, CommentStyle]] =
      settingKey(
        "CommentStyles to be used by file extension they should be applied to; C style block comments for Scala and Java files by default"
      )

    val headerEmptyLine: SettingKey[Boolean] =
      settingKey("If an empty line header should be added between the header and the body")

    val headerSources =
      taskKey[scala.collection.Seq[File]]("Sources which need headers checked or created.")

    val headerResources =
      taskKey[scala.collection.Seq[File]]("Resources which need headers checked or created.")

    val headerCreate: TaskKey[Iterable[File]] =
      taskKey[Iterable[File]]("Create/update headers")

    val headerCreateAll: TaskKey[Iterable[File]] =
      taskKey[Iterable[File]]("Create/update headers in all configurations")

    val headerCheck: TaskKey[Iterable[File]] =
      taskKey[Iterable[File]]("Check whether files have headers")

    val headerCheckAll: TaskKey[Iterable[File]] =
      taskKey[Iterable[File]]("Check whether files have headers in all configurations")

    def headerSettings(configurations: Configuration*): Seq[Setting[_]] =
      configurations.foldLeft(List.empty[Setting[_]])(_ ++ inConfig(_)(toBeScopedSettings))
  }

  import autoImport._

  override def trigger = allRequirements

  override def requires = JvmPlugin

  override def globalSettings = Vector(
    headerEmptyLine    := true,
    headerEndYear      := None,
    headerLicenseStyle := LicenseStyle.Detailed,
    headerMappings := Map(
      FileType.scala -> cStyleBlockComment,
      FileType.java  -> cStyleBlockComment
    ),
    headerLicense := None,
  )

  override def projectSettings = notToBeScopedSettings ++ headerSettings(Compile, Test)

  private def toBeScopedSettings =
    Vector(
      headerSources := collectFiles(
        headerCreate / unmanagedSourceDirectories,
        headerSources / includeFilter,
        headerSources / excludeFilter
      ).value,
      headerResources := collectFiles(
        headerCreate / unmanagedResourceDirectories,
        headerResources / includeFilter,
        headerResources / excludeFilter
      ).value,
      headerCreate := {
        val fallback = headerLicenseFallback.value
        createHeadersTask(
          streams.value.cacheDirectory,
          headerSources.value.toList ++
          headerResources.value.toList,
          headerLicense.value
            .orElse(fallback)
            .getOrElse(sys.error("Unable to auto detect project license")),
          headerMappings.value,
          headerEmptyLine.value,
          streams.value.log
        )
      },
      headerCreateAll := headerCreate.?.all(
        ScopeFilter(configurations = inAnyConfiguration)
      ).value.flatten.flatten.toSet,
      headerCheck := {
        val fallback = headerLicenseFallback.value
        checkHeadersTask(
          headerSources.value.toList ++
          headerResources.value.toList,
          headerLicense.value
            .orElse(fallback)
            .getOrElse(sys.error("Unable to auto detect project license")),
          headerMappings.value,
          headerEmptyLine.value,
          streams.value.log
        )
      },
      headerCheckAll := headerCheck.?.all(
        ScopeFilter(configurations = inAnyConfiguration)
      ).value.flatten.flatten.toSet
    )

  private def notToBeScopedSettings =
    Vector(
      headerLicenseFallback := LicenseDetection(
        licenses.value.toList,
        organizationName.value,
        startYear.value,
        headerEndYear.value,
        headerLicenseStyle.value
      ),
      headerSources / includeFilter   := (unmanagedSources / includeFilter).value,
      headerSources / excludeFilter   := (unmanagedSources / excludeFilter).value,
      headerResources / includeFilter := (unmanagedResources / includeFilter).value,
      headerResources / excludeFilter := (unmanagedResources / excludeFilter).value
    )

  private def createHeadersTask(
      cacheDirectory: File,
      files: Seq[File],
      headerLicense: License,
      headerMappings: Map[FileType, CommentStyle],
      headerEmptyLine: Boolean,
      log: Logger
  ): Iterable[File] =
    FileFunction.cached(cacheDirectory) { files =>
      if (files.nonEmpty)
        createHeaders(files, headerLicense, headerMappings, headerEmptyLine, log)
      else Set.empty
    }(files.toSet)

  private def createHeaders(
      files: Set[File],
      headerLicense: License,
      headerMappings: Map[FileType, CommentStyle],
      headerEmptyLine: Boolean,
      log: Logger
  ): Set[File] = {
    def createHeader(fileType: FileType, commentStyle: CommentStyle)(file: File) = {
      def write(text: String) = Files.write(file.toPath, text.getBytes(UTF_8)).toFile
      log.debug(s"About to create/update header for $file")
      HeaderCreator(
        fileType,
        commentStyle,
        headerLicense,
        headerEmptyLine,
        log,
        Files.newInputStream(file.toPath)
      ).createText
        .map(write)
    }
    val touchedFiles =
      groupFilesByFileTypeAndCommentStyle(files, headerMappings)
        .flatMap { case ((fileType, commentStyle), groupedFiles) =>
          groupedFiles.flatMap(createHeader(fileType, commentStyle))
        }
    if (touchedFiles.nonEmpty)
      log.info(
        s"Headers created for ${touchedFiles.size} files:$NewLine  ${touchedFiles.mkString(s"$NewLine  ")}"
      )
    touchedFiles.toSet
  }

  private def checkHeadersTask(
      files: Seq[File],
      headerLicense: License,
      headerMappings: Map[FileType, CommentStyle],
      headerEmptyLine: Boolean,
      log: Logger
  ) = {
    def checkHeader(fileType: FileType, commentStyle: CommentStyle)(file: File) =
      HeaderCreator(
        fileType,
        commentStyle,
        headerLicense,
        headerEmptyLine,
        log,
        Files.newInputStream(file.toPath)
      ).createText
        .map(_ => file)
    val filesWithoutHeader =
      groupFilesByFileTypeAndCommentStyle(files, headerMappings)
        .flatMap { case ((fileType, commentStyle), groupedFiles) =>
          groupedFiles.flatMap(checkHeader(fileType, commentStyle))
        }
    if (filesWithoutHeader.nonEmpty)
      throw new MessageOnlyException(
        s"""|There are files without headers!
            |  ${filesWithoutHeader.mkString(s"$NewLine  ")}
            |""".stripMargin
      )
    else Nil
  }

  private def groupFilesByFileTypeAndCommentStyle(
      files: Iterable[File],
      headerMappings: Map[FileType, CommentStyle]
  ) =
    files
      .groupBy(_.extension)
      .collect {
        case (Some(ext), groupedFiles) =>
          headerMappings.collect {
            case key @ (FileType(`ext`, _, ""), _) =>
              key -> groupedFiles.filter(_.isFile)
            case key @ (FileType(`ext`, _, name), _) =>
              key -> groupedFiles.filter(file => file.getName == s"$name.$ext" && file.isFile)
          }
        case (None, groupedFiles) =>
          headerMappings.collect { case key @ (FileType("", _, name), _) =>
            key -> groupedFiles.filter(file => file.getName == name && file.isFile)
          }
      }
      .flatten
}
