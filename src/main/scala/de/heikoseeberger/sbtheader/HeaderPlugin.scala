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

import java.io.File
import java.nio.charset.StandardCharsets.UTF_8
import java.nio.file.Files

import de.heikoseeberger.sbtheader.CommentStyle.CStyleBlockComment
import sbt.Keys.{
  licenses,
  organizationName,
  startYear,
  streams,
  unmanagedResources,
  unmanagedSources
}
import sbt.plugins.JvmPlugin
import sbt.{
  inConfig,
  settingKey,
  taskKey,
  AutoPlugin,
  Compile,
  Configuration,
  Logger,
  Setting,
  SettingKey,
  TaskKey,
  Test
}

import scala.util.matching.Regex

object HeaderPlugin extends AutoPlugin {

  final object autoImport {

    val HeaderLicense = License

    object HeaderPattern {

      def commentBetween(start: String, middle: String, end: String): Regex =
        new Regex(raw"""(?s)($start(?!\$middle).*?$end(?:\n|\r|\r\n)+)(.*)""")

      def commentStartingWith(start: String): Regex =
        new Regex(
          raw"""(?s)((?:$start[^\n\r]*(?:\n|\r|\r\n))*(?:$start[^\n\r]*(?:(?:\n){2,}|(?:\r){2,}|(?:\r\n){2,})+))(.*)"""
        )
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

    val headerCreate: TaskKey[Iterable[File]] =
      taskKey[Iterable[File]]("Create/update headers")

    val headerCheck: TaskKey[Iterable[File]] =
      taskKey[Iterable[File]]("Check whether files have headers")

    def headerSettings(configurations: Configuration*): Seq[Setting[_]] =
      configurations.foldLeft(List.empty[Setting[_]]) {
        _ ++ inConfig(_)(toBeScopedSettings)
      }
  }
  import autoImport._

  override def trigger = allRequirements

  override def requires = JvmPlugin

  override def projectSettings = notToBeScopedSettings ++ headerSettings(Compile, Test)

  private def toBeScopedSettings =
    Vector(
      unmanagedSources in headerCreate := unmanagedSources.value,
      unmanagedResources in headerCreate := unmanagedResources.value,
      headerCreate := createHeadersTask(
        unmanagedSources
          .in(headerCreate)
          .value
          .toList ++ unmanagedResources.in(headerCreate).value.toList,
        headerLicense.value
          .getOrElse(
            sys.error("Unable to auto detect project license")
          ),
        headerMappings.value,
        streams.value.log
      ),
      headerCheck := checkHeadersTask(
        unmanagedSources
          .in(headerCreate)
          .value
          .toList ++ unmanagedResources.in(headerCreate).value.toList,
        headerLicense.value
          .getOrElse(
            sys.error("Unable to auto detect project license")
          ),
        headerMappings.value,
        streams.value.log
      )
    )

  private def notToBeScopedSettings =
    Vector(
      headerMappings := Map(
        FileType.scala -> CStyleBlockComment,
        FileType.java  -> CStyleBlockComment
      ),
      headerLicense := LicenseDetection(licenses.value.toList,
                                        organizationName.value,
                                        startYear.value)
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
      groupFilesByCommentStyle(files, headerMappings)
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
    val filesWithoutHeader = groupFilesByCommentStyle(files, headerMappings)
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

  private def groupFilesByCommentStyle(files: Seq[File], headers: Map[FileType, CommentStyle]) =
    files
      .groupBy(_.extension)
      .collect {
        case (Some(ext), groupedFiles) =>
          headers
            .find { case (FileType(extension, _), _) => extension == ext }
            .map(_ -> groupedFiles)
      }
      .flatten
}
