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

import java.io.InputStream
import sbt.Logger
import scala.io.Codec

object HeaderCreator {

  def apply(fileType: FileType,
            commentStyle: CommentStyle,
            license: License,
            log: Logger,
            input: InputStream): HeaderCreator =
    new HeaderCreator(fileType, commentStyle, license, log, input)
}

final class HeaderCreator private (fileType: FileType,
                                   commentStyle: CommentStyle,
                                   license: License,
                                   log: Logger,
                                   input: InputStream) {

  private val crlf          = """(?s)(?:.*)(\r\n)(?:.*)""".r
  private val cr            = """(?s)(?:.*)(\r)(?:.*)""".r
  private val headerText    = commentStyle(license)
  private val headerPattern = commentStyle.pattern

  private val (firstLine, text) = {
    val fileContent = try {
      scala.io.Source.fromInputStream(input)(Codec.UTF8).mkString
    } finally {
      input.close()
    }
    fileType.firstLinePattern match {
      case Some(pattern) =>
        fileContent match {
          case pattern(first, rest) => (first, rest)
          case other                => ("", other)
        }
      case _ => ("", fileContent)
    }
  }

  log.debug(s"First line of file is:$newLine$firstLine")
  log.debug(s"Text of file is:$newLine$text")

  private val fileNewLine =
    text match {
      case crlf(_) => "\r\n"
      case cr(_)   => "\r"
      case _       => "\n"
    }

  private val headerNewLine =
    headerText match {
      case crlf(_) => "\r\n"
      case cr(_)   => "\r"
      case _       => "\n"
    }

  private val newHeaderText = headerText.replace(headerNewLine, fileNewLine)

  private val modifiedText =
    text match {
      case headerPattern(`headerText`, _) => None
      case headerPattern(_, body)         => Some(firstLine + newHeaderText + body)
      case body if body.isEmpty           => None
      case body                           => Some(firstLine + newHeaderText + body.replaceAll("""^\s+""", "")) // Trim left
    }
  log.debug(s"Modified text of file is:$newLine$modifiedText")

  def createText: Option[String] =
    modifiedText
}
