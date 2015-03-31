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

object HeaderPattern {
  val cStyleBlockComment = """(?s)(/\*(?!\*).*?\*/(?:\n|\r|\r\n)+)(.*)""".r
  val hashLineComment = """(?s)((?:#[^\n\r]*(?:\n|\r|\r\n))+(?:\n|\r|\r\n)+)(.*)""".r
}

object HeaderKey {
  val headers = settingKey[Map[String, (Regex, String)]]("Header pattern and text by extension; empty by default")
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

  private val shebangAndBody = """(#!.*(?:\n|(?:\r\n))+)((?:.|\n|(?:\r\n))*)""".r

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
      (unmanagedSources in createHeaders).value.toList ++ (unmanagedResources in createHeaders).value.toList,
      headers.value,
      streams.value.log
    )
  )

  def notToBeScopedSettings: Seq[Setting[_]] = List(
    headers := Map.empty
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
