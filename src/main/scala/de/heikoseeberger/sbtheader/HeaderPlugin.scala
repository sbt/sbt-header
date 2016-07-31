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
import sbt._
import sbt.Keys._
import scala.collection.JavaConversions._
import scala.util.matching.Regex
import java.io.FileInputStream

object HeaderPattern {
  val cStyleBlockComment = commentBetween("""/\*+""", "*", """\*/""")
  val cppStyleLineComment = commentStartingWith("//")
  val hashLineComment = commentStartingWith("#")

  def commentBetween(start: String, middle: String, end: String): Regex =
    new Regex(raw"""(?s)($start(?!\$middle).*?$end(?:\n|\r|\r\n)+)(.*)""")
  def commentStartingWith(start: String): Regex =
    new Regex(raw"""(?s)((?:$start[^\n\r]*(?:\n|\r|\r\n))*(?:#[^\n\r]*(?:(?:\n){2,}|(?:\r){2,}|(?:\r\n){2,})+))(.*)""")
}

object CommentStyleMapping {
  import de.heikoseeberger.sbtheader.license._

  val JavaBlockComments = "java" -> "*"
  val ScalaBlockComments = "scala" -> "*"

  def createFrom(
    license: License,
    yyyy: String,
    copyrightOwner: String,
    mappings: Seq[(String, String)] = Seq(JavaBlockComments, ScalaBlockComments)
  ): Map[String, (Regex, String)] = {
    mappings.map { mapping => mapping._1 -> license(yyyy, copyrightOwner, mapping._2) }.toMap
  }
}

object HeaderKey {
  val headers = settingKey[Map[String, (Regex, String)]]("Header pattern and text by extension; empty by default")
  val excludes = settingKey[Seq[String]]("File patterns for files to be excluded; empty by default")
  val createHeaders = taskKey[Iterable[File]]("Create/update headers")
}

/**
 * Enable this plugin to automate header creation/update on compile. By default the `Compile` and `Test` configurations
 * are considered; use [[AutomateHeaderPlugin.automateFor]] to add further ones.
 */
object AutomateHeaderPlugin extends AutoPlugin {

  override def requires = HeaderPlugin

  override def projectSettings = automateFor(Compile, Test)

  def automateFor(configurations: Configuration*): Seq[Setting[_]] = configurations.foldLeft(List.empty[Setting[_]]) {
    _ ++ inConfig(_)(compile := compile.dependsOn(HeaderKey.createHeaders).value)
  }
}

/**
 * This plugin adds the [[HeaderKey.createHeaders]] task to created/update headers. The patterns and
 * texts for the headers are specified via [[HeaderKey.headers]].
 */
object HeaderPlugin extends AutoPlugin {

  val autoImport = HeaderKey

  import autoImport._

  override def trigger = allRequirements

  override def requires = plugins.JvmPlugin

  override def projectSettings = settingsFor(Compile, Test) ++ notToBeScopedSettings

  def settingsFor(configurations: Configuration*): Seq[Setting[_]] = configurations.foldLeft(List.empty[Setting[_]]) {
    _ ++ inConfig(_)(toBeScopedSettings)
  }

  def toBeScopedSettings: Seq[Setting[_]] = List(
    unmanagedSources in createHeaders := unmanagedSources.value,
    unmanagedResources in createHeaders := unmanagedResources.value,
    createHeaders := createHeadersTask(
      FileFilter(excludes.value).filter(
        (unmanagedSources in createHeaders).value.toList ++ (unmanagedResources in createHeaders).value.toList
      ),
      headers.value,
      streams.value.log
    )
  )

  def notToBeScopedSettings: Seq[Setting[_]] = List(
    headers := Map.empty,
    excludes := Seq.empty
  )

  private def createHeadersTask(files: Seq[File], headers: Map[String, (Regex, String)], log: Logger) = {
    val touchedFiles = files
      .groupBy(_.extension)
      .collect { case (Some(ext), groupedFiles) => headers.get(ext).map(_ -> groupedFiles) }
      .flatten
      .flatMap { case ((pattern, text), groupedFiles) => groupedFiles.flatMap(createHeader(pattern, text, log)) }
    if (touchedFiles.nonEmpty)
      log.info(s"Headers created for ${touchedFiles.size} files:$newLine  ${touchedFiles.mkString(s"$newLine  ")}")
    touchedFiles
  }

  private def createHeader(headerPattern: Regex, headerText: String, log: Logger)(file: File) = {
    def write(text: String) = Files.write(file.toPath, text.getBytes(UTF_8)).toFile
    log.debug(s"About to create/update header for $file")
    HeaderCreator(headerPattern, headerText, log, new FileInputStream(file)).getText.map(write)
  }
}
