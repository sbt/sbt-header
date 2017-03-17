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

import de.heikoseeberger.sbtheader.HeaderPlugin.autoImport.HeaderPattern._

import scala.util.matching.Regex

/**
  * Representation of the different comment styles supported by this plugin.
  */
sealed trait CommentStyle {
  def pattern: Regex

  def apply(licenseText: String): String

  def apply(license: License): String = apply(license.text)
}

object CommentStyle {

  final case object CStyleBlockComment extends CommentStyle {
    private val commentBlock = new CommentBlock("/*", " *", " */" + newLine)

    override def apply(licenseText: String) = commentBlock(licenseText)

    override val pattern: Regex = commentBetween("""/\*+""", "*", """\*/""")
  }

  case object CppStyleLineComment extends CommentStyle {
    private val commentBlock = new CommentBlock("", "//", "")

    override def apply(licenseText: String) = commentBlock(licenseText)

    override val pattern: Regex = commentStartingWith("//")
  }

  case object HashLineComment extends CommentStyle {
    private val commentBlock = new CommentBlock("", "#", "")

    override def apply(licenseText: String) = commentBlock(licenseText)

    override val pattern: Regex = commentStartingWith("#")
  }

  case object TwirlStyleComment extends CommentStyle {
    private val commentStyle = new CommentBlock("@*", " *", " *@" + newLine)

    override def apply(licenseText: String) = commentStyle(licenseText)

    override val pattern: Regex = commentBetween("""@\*""", "*", """\*@""")
  }

  case object TwirlStyleBlockComment extends CommentStyle {
    override def apply(licenseText: String) = TwirlCommentBlock(licenseText)

    override val pattern: Regex = commentBetween("""@\*+""", "*", """\*@""")
  }

}

private object TwirlCommentBlock {

  def apply(text: String): String = {
    val maxLineLength = text.lines.map(_.length).max

    def fillLine(line: String) =
      " * " + line + spaces(maxLineLength - line.length) + " *"

    val commentBlock = text.lines.map(fillLine).mkString(newLine)
    val firstLine    = "@**" + stars(maxLineLength + 2)
    val lastLine     = " " + firstLine.reverse

    firstLine ++ newLine ++ commentBlock ++ newLine ++ lastLine ++ newLine ++ newLine
  }

  private def spaces(count: Int) = " " * count

  private def stars(count: Int) = "*" * count
}

private final class CommentBlock(blockPrefix: String, linePrefix: String, blockSuffix: String) {

  def apply(text: String): String = {
    def prependWithLinePrefix(s: String) =
      s match {
        case ""   => linePrefix
        case line => linePrefix + " " + line
      }

    var commentBlock = text.lines.map(prependWithLinePrefix).mkString(newLine)
    if (!blockPrefix.isEmpty) commentBlock = blockPrefix + newLine + commentBlock
    commentBlock ++ newLine ++ blockSuffix ++ newLine
  }
}
