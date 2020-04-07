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

import de.heikoseeberger.sbtheader.HeaderPlugin.autoImport.HeaderPattern.{
  commentBetween,
  commentStartingWith
}
import scala.util.matching.Regex

/**
  * Representation of the different comment styles supported by this plugin.
  */
final case class CommentStyle(commentCreator: CommentCreator, pattern: Regex) {

  def apply(licenseText: String, existingHeader: Option[String]): String =
    commentCreator(licenseText, existingHeader) + newLine + newLine

  def apply(license: License, existingHeader: Option[String]): String =
    apply(license.text, existingHeader)

  def apply(licenseText: String): String =
    apply(licenseText, None)

  def apply(license: License): String =
    apply(license.text)
}

trait CommentCreator {
  def apply(text: String, existingText: Option[String] = None): String

  def apply(text: String): String = apply(text, None)
}

object CommentStyle {

  val cStyleBlockComment = CommentStyle(
    new CommentBlockCreator("/*", " *", " */"),
    commentBetween("""/\*+""", "*", """\*/""")
  )

  val cppStyleLineComment = CommentStyle(new LineCommentCreator("//"), commentStartingWith("//"))

  val hashLineComment = CommentStyle(new LineCommentCreator("#"), commentStartingWith("#"))

  val twirlStyleBlockComment = CommentStyle(
    new CommentBlockCreator("@*", " *", " *@"),
    commentBetween("""@\*""", "*", """\*@""")
  )

  val twirlStyleFramedBlockComment =
    CommentStyle(TwirlStyleFramedBlockCommentCreator, commentBetween("""@\*+""", "*", """\*@"""))

  val xmlStyleBlockComment =
    CommentStyle(new CommentBlockCreator("<!--", "  ", "-->"), commentBetween("<!--", "  ", "-->"))

}

object TwirlStyleFramedBlockCommentCreator extends CommentCreator {

  def apply(text: String, existingText: Option[String]): String = {
    val maxLineLength = text.linesIterator.map(_.length).max

    def fillLine(line: String) =
      " * " + line + spaces(maxLineLength - line.length) + " *"

    val commentBlock = text.linesIterator.map(fillLine).mkString(newLine)
    val firstLine    = "@**" + stars(maxLineLength + 2)
    val lastLine     = " " + firstLine.reverse

    firstLine ++ newLine ++ commentBlock ++ newLine ++ lastLine
  }

  private def spaces(count: Int) = " " * count

  private def stars(count: Int) = "*" * count
}

final class LineCommentCreator(linePrefix: String) extends CommentCreator {

  override def apply(text: String, existingText: Option[String]): String = {
    def prependWithLinePrefix(s: String) =
      s match {
        case ""   => if (!linePrefix.trim.isEmpty) linePrefix else ""
        case line => linePrefix + " " + line
      }

    text.linesIterator.map(prependWithLinePrefix).mkString(newLine)
  }
}

final class CommentBlockCreator(blockPrefix: String, linePrefix: String, blockSuffix: String)
    extends CommentCreator {

  private val lineCommentCreator = new LineCommentCreator(linePrefix)

  def apply(text: String, existingText: Option[String]): String =
    blockPrefix + newLine + lineCommentCreator(text) + newLine + blockSuffix
}

object IdentityCommentCreator extends CommentCreator {
  override def apply(text: String, existingText: Option[String]) = text
}
