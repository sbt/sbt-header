/*
 * Copyright 2015 Heiko Seeberger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
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

object SbtHeader extends AutoPlugin {

  object autoImport {

    object HeaderPattern {
      val cStyleBlockComment = """(?s)(/\*(?!\*).*?\*/(?:\n|\r|\r\n)+)(.*)""".r
      val hashLineComment = """(?s)((?:#[^\n\r]*(?:\n|\r|\r\n))+(?:\n|\r|\r\n)+)(.*)""".r
    }

    val headers = settingKey[Map[String, (Regex, String)]]("Header pattern and text by extension; empty by default")
    val createHeaders = taskKey[Iterable[File]]("Create/update headers")
  }

  import autoImport._

  private val shebangAndBody = """(#!.*(?:\n|(?:\r\n))+)((?:.|\n|(?:\r\n))*)""".r

  override def trigger = allRequirements

  override def requires = plugins.CorePlugin

  override def projectSettings =
    inConfig(Compile)(toBeScopedSettings) ++ inConfig(Test)(toBeScopedSettings) ++ notToBeScopedSettings

  def toBeScopedSettings = List(
    unmanagedSources in createHeaders := unmanagedSources.value,
    unmanagedResources in createHeaders := unmanagedResources.value,
    createHeaders := createHeadersTask(
      (unmanagedSources in createHeaders).value.toList ++ (unmanagedResources in createHeaders).value.toList,
      headers.value,
      streams.value.log
    )
  )

  def notToBeScopedSettings = List(
    headers := Map.empty
  )

  def automate = Seq(
    (compile in Compile) <<= (compile in Compile) dependsOn (createHeaders in Compile),
    (compile in Test) <<= (compile in Test) dependsOn (createHeaders in Test)
  )

  private def createHeadersTask(files: Seq[File], headers: Map[String, (Regex, String)], log: Logger) = {
    val touchedFiles = files
      .groupBy(_.extension)
      .collect { case (Some(ext), groupedFiles) => headers.get(ext).map(_ -> groupedFiles) }
      .flatten
      .flatMap { case ((pattern, text), groupedFiles) => groupedFiles.flatMap(createHeader(pattern, text, log)) }
    if (!touchedFiles.isEmpty)
      log.info(s"Headers created for ${touchedFiles.size} files:$newLine  ${touchedFiles.mkString(s"$newLine  ")}")
    touchedFiles
  }

  private def createHeader(headerPattern: Regex, headerText: String, log: Logger)(file: File) = {
    def write(text: String) = Files.write(file.toPath, text.split(newLine).toList, UTF_8).toFile
    log.debug(s"About to create/update header for $file")
    val (firstLine, text) = Files.readAllLines(file.toPath, UTF_8).mkString(newLine) match {
      case shebangAndBody(s, b) => (s, b)
      case other                => ("", other)
    }
    log.debug(s"First line of $file is:$newLine$firstLine")
    log.debug(s"Text of $file is:$newLine$text")
    val modifiedText = text match {
      case headerPattern(`headerText`, _) => None
      case headerPattern(_, body)         => Some(firstLine + headerText + body)
      case body if body.isEmpty           => None
      case body                           => Some(firstLine + headerText + body.replaceAll("""^\s+""", "")) // Trim left
    }
    log.debug(s"Modified text of $file is:$newLine$modifiedText")
    modifiedText.map(write)
  }
}
