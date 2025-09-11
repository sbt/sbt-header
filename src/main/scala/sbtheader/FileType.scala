/*
 * Copyright (c) 2015 - 2025, Heiko Seeberger
 * Copyright (c) 2025, sbt plugin contributors
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package sbtheader

import scala.util.matching.Regex

final case class FileType(
    extension: String,
    firstLinePattern: Option[Regex] = None,
    name: String = ""
)

object FileType {

  val conf: FileType =
    FileType("conf")

  val java: FileType =
    FileType("java")

  val scala: FileType =
    FileType("scala")

  val sh: FileType =
    FileType("sh", Some(firstLinePattern("#!.*")))

  val xml: FileType =
    FileType("xml", Some(firstLinePattern("<\\?xml.*\\?>")))

  private def firstLinePattern(firstLinePattern: String) =
    s"""($firstLinePattern(?:\\s+))([\\S\\s]*)""".r
}
