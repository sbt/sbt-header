/*
 * Copyright 2015 Heiko Seeberger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.heikoseeberger.sbtheader

import de.heikoseeberger.sbtheader.CommentStyle.cStyleBlockComment
import java.nio.charset.StandardCharsets.UTF_8
import java.nio.file.Files

import sbt.{
  AutoPlugin,
  Compile,
  Configuration,
  File,
  Logger,
  Setting,
  SettingKey,
  TaskKey,
  Test,
  inConfig,
  settingKey,
  taskKey
}
import sbt.Defaults.collectFiles
import sbt.Keys._
import sbt.plugins.JvmPlugin

import scala.util.matching.Regex

object HeaderPlugin extends AutoPlugin {

  final object autoImport {

    val HeaderLicense = License

    final object HeaderPattern {

      def commentBetween(start: String, middle: String, end: String): Regex =
        raw"""(?s)($start(?!\$middle).*?$end(?:\n|\r|\r\n)+)(.*)""".r

      def commentStartingWith(start: String): Regex =
        raw"""(?s)((?:$start[^\n\r]*(?:\n|\r|\r\n))*(?:$start[^\n\r]*(?:(?:\n){2,}|(?:\r){2,}|(?:\r\n){2,})+))(.*)""".r
    }

    val HeaderFileType = FileType

    val HeaderCommentStyle = CommentStyle

    val headerLicense: SettingKey[Option[License]] =
      settingKey(
        "The license to apply to files; None by default (enabling auto detection from project settings)"
      )

    val headerMappings: SettingKey[Map[FileType, CommentStyle]] =
      settingKey(
        "CommentStyles to be used by file extension they should be applied to; C style block comments for Scala and Java files by default"
      )

    val headerSources =
      taskKey[scala.collection.Seq[File]]("Sources which need headers checked or created.")

    val headerResources =
      taskKey[scala.collection.Seq[File]]("Resources which need headers checked or created.")

    val headerCreate: TaskKey[Iterable[File]] =
      taskKey[Iterable[File]]("Create/update headers")

    val headerCheck: TaskKey[Iterable[File]] =
      taskKey[Iterable[File]]("Check whether files have headers")

    def headerSettings(configurations: Configuration*): Seq[Setting[_]] =
      configurations.foldLeft(List.empty[Setting[_]]) { _ ++ inConfig(_)(toBeScopedSettings) }
  }

  import autoImport._

  override def trigger = allRequirements

  override def requires = JvmPlugin

  override def projectSettings = notToBeScopedSettings ++ headerSettings(Compile, Test)

  private def toBeScopedSettings =
    Vector(
      headerSources := collectFiles(
        unmanagedSourceDirectories.in(headerCreate),
        includeFilter.in(headerSources),
        excludeFilter.in(headerSources)
      ).value,
      headerResources := collectFiles(
        unmanagedResourceDirectories.in(headerCreate),
        includeFilter.in(headerResources),
        excludeFilter.in(headerResources)
      ).value,
      headerCreate := createHeadersTask(
        headerSources.value.toList ++
        headerResources.value.toList,
        headerLicense.value.getOrElse(sys.error("Unable to auto detect project license")),
        headerMappings.value,
        streams.value.log
      ),
      headerCheck := checkHeadersTask(
        headerSources.value.toList ++
        headerResources.value.toList,
        headerLicense.value.getOrElse(sys.error("Unable to auto detect project license")),
        headerMappings.value,
        streams.value.log
      )
    )

  private def notToBeScopedSettings =
    Vector(
      headerMappings := Map(
        FileType.scala -> cStyleBlockComment,
        FileType.java  -> cStyleBlockComment
      ),
      headerLicense := LicenseDetection(licenses.value.toList,
                                        organizationName.value,
                                        startYear.value),
      includeFilter.in(headerSources) := includeFilter.in(unmanagedSources).value,
      excludeFilter.in(headerSources) := excludeFilter.in(unmanagedSources).value,
      includeFilter.in(headerResources) := includeFilter.in(unmanagedResources).value,
      excludeFilter.in(headerResources) := excludeFilter.in(unmanagedResources).value
    )

  private def createHeadersTask(files: Seq[File],
                                headerLicense: License,
                                headerMappings: Map[FileType, CommentStyle],
                                log: Logger) = {
    def createHeader(fileType: FileType, commentStyle: CommentStyle)(file: File) = {
      def write(text: String) = Files.write(file.toPath, text.getBytes(UTF_8)).toFile
      log.debug(s"About to create/update header for $file")
      HeaderCreator(fileType, commentStyle, headerLicense, log, Files.newInputStream(file.toPath)).createText
        .map(write)
    }
    val touchedFiles =
      groupFilesByFileTypeAndCommentStyle(files, headerMappings)
        .flatMap {
          case ((fileType, commentStyle), groupedFiles) =>
            groupedFiles.flatMap(createHeader(fileType, commentStyle))
        }
    if (touchedFiles.nonEmpty)
      log.info(
        s"Headers created for ${touchedFiles.size} files:$newLine  ${touchedFiles.mkString(s"$newLine  ")}"
      )
    touchedFiles
  }

  private def checkHeadersTask(files: Seq[File],
                               headerLicense: License,
                               headerMappings: Map[FileType, CommentStyle],
                               log: Logger) = {
    def checkHeader(fileType: FileType, commentStyle: CommentStyle)(file: File) =
      HeaderCreator(fileType, commentStyle, headerLicense, log, Files.newInputStream(file.toPath)).createText
        .map(_ => file)
    val filesWithoutHeader =
      groupFilesByFileTypeAndCommentStyle(files, headerMappings)
        .flatMap {
          case ((fileType, commentStyle), groupedFiles) =>
            groupedFiles.flatMap(checkHeader(fileType, commentStyle))
        }
    if (filesWithoutHeader.nonEmpty)
      sys.error(
        s"""|There are files without headers!
            |  ${filesWithoutHeader.mkString(s"$newLine  ")}
            |""".stripMargin
      )
    filesWithoutHeader
  }

  private def groupFilesByFileTypeAndCommentStyle(files: Seq[File],
                                                  headerMappings: Map[FileType, CommentStyle]) =
    files
      .groupBy(_.extension)
      .collect {
        case (Some(ext), groupedFiles) =>
          headerMappings.collect {
            case key @ (FileType(`ext`, _), _) => key -> groupedFiles
          }
      }
      .flatten
}
