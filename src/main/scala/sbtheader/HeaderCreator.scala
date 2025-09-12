/*
 * Copyright (c) 2015 - 2025, Heiko Seeberger
 * Copyright (c) 2025, sbt plugin contributors
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package sbtheader

import java.io.InputStream
import sbt.Logger
import scala.io.Codec

object HeaderCreator {

  def apply(
      fileType: FileType,
      commentStyle: CommentStyle,
      license: sbtheader.License,
      headerEmptyLine: Boolean,
      log: Logger,
      input: InputStream
  ): HeaderCreator =
    new HeaderCreator(fileType, commentStyle, license, headerEmptyLine, log, input)
}

final class HeaderCreator private (
    fileType: FileType,
    commentStyle: CommentStyle,
    license: sbtheader.License,
    headerEmptyLine: Boolean,
    log: Logger,
    input: InputStream
) {
  private val crlf          = """(?s)(?:.*)(\r\n)(?:.*)""".r
  private val cr            = """(?s)(?:.*)(\r)(?:.*)""".r
  private val headerPattern = commentStyle.pattern

  private val (firstLine, text) = {
    val fileContent =
      try scala.io.Source.fromInputStream(input)(using Codec.UTF8).mkString
      finally input.close()

    fileType.firstLinePattern match {
      case Some(pattern) =>
        fileContent match {
          case pattern(first, rest) => (first, rest)
          case other                => ("", other)
        }

      case _ =>
        ("", fileContent)
    }
  }

  private val modifiedText =
    text match {
      case headerPattern(existingText, body) =>
        val newText = newHeaderText(Some(existingText))
        if (newText == existingText) None
        else Some(firstLine + newText + body.replaceAll("""^\s+""", "")) // Trim left

      case body if body.isEmpty =>
        None

      case body =>
        Some(firstLine + newHeaderText(None) + body.replaceAll("""^\s+""", "")) // Trim left
    }

  def createText: Option[String] =
    modifiedText

  private def newHeaderText(existingHeader: Option[String]) = {
    val suffix        = if (headerEmptyLine) "" else NewLine
    val headerText    = commentStyle(license, existingHeader).stripSuffix(suffix)
    val headerNewLine = newLine(headerText)
    headerText.replace(headerNewLine, newLine(text))
  }

  private def newLine(s: String) =
    s match {
      case crlf(_) => "\r\n"
      case cr(_)   => "\r"
      case _       => "\n"
    }
}
