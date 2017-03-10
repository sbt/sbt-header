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

import java.io.{ File, FileInputStream }
import java.nio.charset.StandardCharsets.UTF_8
import java.nio.file.Files

import de.heikoseeberger.sbtheader.CommentStyle.CStyleBlockComment
import sbt.Keys.{ compile, streams, unmanagedResources, unmanagedSources }
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

import scala.collection.breakOut
import scala.util.matching.Regex

object HeaderPlugin extends AutoPlugin {

  final object autoImport {

    object HeaderLicense extends Licenses

    object HeaderPattern {

      val cStyleBlockComment: Regex =
        commentBetween("""/\*+""", "*", """\*/""")

      val cppStyleLineComment: Regex =
        commentStartingWith("//")

      val hashLineComment: Regex =
        commentStartingWith("#")

      val twirlBlockComment: Regex =
        commentBetween("""@\*+""", "*", """\*@""")

      val twirlStyleComment: Regex =
        commentBetween("""@\*""", "*", """\*@""")

      def commentBetween(start: String, middle: String, end: String): Regex =
        new Regex(raw"""(?s)($start(?!\$middle).*?$end(?:\n|\r|\r\n)+)(.*)""")

      def commentStartingWith(start: String): Regex =
        new Regex(
          raw"""(?s)((?:$start[^\n\r]*(?:\n|\r|\r\n))*(?:#[^\n\r]*(?:(?:\n){2,}|(?:\r){2,}|(?:\r\n){2,})+))(.*)"""
        )
    }

    object HeaderCommentStyleMapping {

      val javaBlockComments: (String, CommentStyle) =
        "java" -> CStyleBlockComment

      val scalaBlockComments: (String, CommentStyle) =
        "scala" -> CStyleBlockComment

      def createFrom(
          license: License,
          mappings: Seq[(String, CommentStyle)] = Vector(javaBlockComments, scalaBlockComments)
      ): Map[String, (Regex, String)] =
        mappings.map {
          case (headerPattern, commentStyle) => headerPattern -> commentStyle(license)
        }(breakOut)
    }

    val headerLicense: SettingKey[License] =
      settingKey("The license to apply to files")

    val headerMappings: SettingKey[Map[String, CommentStyle]] =
      settingKey(
        "CommentStyles to be used by file extension they should be applied to; empty by default"
      )

    /*
     * Since we override the default (mutable) Seq in our package object with the immutable variant, we explicitly use
     * the default (mutable) Seq here. Otherwise users would have to import scala.collection.immutable.Seq explicitly
     * in their build files, which would probably only cause confusion.
     */
    val headerExcludes: SettingKey[scala.collection.Seq[String]] =
      settingKey("File patterns for files to be excluded; empty by default")

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
        FileFilter(Vector(headerExcludes.value: _*)).filter(
          unmanagedSources
            .in(headerCreate)
            .value
            .toList ++ unmanagedResources.in(headerCreate).value.toList
        ),
        headerLicense.value,
        headerMappings.value,
        streams.value.log
      ),
      headerCheck := checkHeadersTask(
        FileFilter(Vector(headerExcludes.value: _*)).filter(
          unmanagedSources
            .in(headerCreate)
            .value
            .toList ++ unmanagedResources.in(headerCreate).value.toList
        ),
        headerLicense.value,
        headerMappings.value,
        streams.value.log
      )
    )

  private def notToBeScopedSettings =
    Vector(
      headerMappings := Map.empty,
      headerExcludes := Seq.empty
    )

  private def createHeadersTask(files: Seq[File],
                                headerLicense: License,
                                headerMappings: Map[String, CommentStyle],
                                log: Logger) = {
    def createHeader(commentStyle: CommentStyle)(file: File) = {
      def write(text: String) = Files.write(file.toPath, text.getBytes(UTF_8)).toFile
      log.debug(s"About to create/update header for $file")
      val (headerPattern, headerText) = commentStyle.apply(headerLicense)
      HeaderCreator(headerPattern, headerText, log, new FileInputStream(file)).createText
        .map(write)
    }
    val touchedFiles =
      groupFilesByCommentStyle(files, headerMappings)
        .flatMap {
          case (commentStyle, groupedFiles) =>
            groupedFiles.flatMap(createHeader(commentStyle))
        }
    if (touchedFiles.nonEmpty)
      log.info(
        s"Headers created for ${touchedFiles.size} files:$newLine  ${touchedFiles.mkString(s"$newLine  ")}"
      )
    touchedFiles
  }

  private def checkHeadersTask(files: Seq[File],
                               headerLicense: License,
                               headerMappings: Map[String, CommentStyle],
                               log: Logger) = {
    def checkHeader(commentStyle: CommentStyle)(file: File) = {
      val (headerPattern, headerText) = commentStyle.apply(headerLicense)
      HeaderCreator(headerPattern, headerText, log, new FileInputStream(file)).createText
        .map(_ => file)
    }
    val filesWithoutHeader = groupFilesByCommentStyle(files, headerMappings)
      .flatMap {
        case (commentStyle, groupedFiles) =>
          groupedFiles.flatMap(checkHeader(commentStyle))
      }
    if (filesWithoutHeader.nonEmpty)
      sys.error(
        s"""|There are files without headers!
          |  ${filesWithoutHeader.mkString(s"$newLine  ")}
          |""".stripMargin
      )
    filesWithoutHeader
  }

  private def groupFilesByCommentStyle(files: Seq[File], headers: Map[String, CommentStyle]) =
    files
      .groupBy(_.extension)
      .collect { case (Some(ext), groupedFiles) => headers.get(ext).map(_ -> groupedFiles) }
      .flatten
}
