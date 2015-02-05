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
import java.nio.file.Files
import sbt._
import scala.collection.JavaConversions._

object SbtHeader extends AutoPlugin {

  object autoImport {
    val headers = settingKey[Map[String, String]]("""Header text by extension; empty by default""")
    val createHeaders = taskKey[Iterable[File]]("Create/update headers")
  }

  override def projectSettings =
    List(
      Keys.sources in autoImport.createHeaders := (Keys.sources in Compile).value,
      autoImport.headers := Map.empty,
      autoImport.createHeaders := createHeaders(
        (Keys.sources in autoImport.createHeaders).value.toList,
        autoImport.headers.value,
        Keys.streams.value.log
      )
    )

  override def trigger = allRequirements

  private def createHeaders(sources: Seq[File], headers: Map[String, String], log: Logger): Iterable[File] = {
    val touchedFiles = sources
      .groupBy(_.extension)
      .collect { case (Some(ext), files) => headers.get(ext).map(_ -> files) }
      .flatten
      .flatMap { case (header, files) => files.flatMap(createHeader(header)) }
    log.info(s"Headers created for ${touchedFiles.size} files${if (touchedFiles.isEmpty) "" else f":%n  " + touchedFiles.mkString(f"%n  ")}")
    touchedFiles
  }

  private def createHeader(header: String)(file: File): Option[File] = {
    val lines = Files.readAllLines(file.toPath)
    if (lines.mkString(f"%n").startsWith(header))
      None
    else
      Some(Files.write(file.toPath, header +: lines).toFile)
  }
}
