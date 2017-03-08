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

private object CommentBlock {

  val cStyle: CommentBlock =
    new CommentBlock("/*", " *", " */" + System.lineSeparator())

  val hashLines: CommentBlock =
    new CommentBlock("", "#", "")

  val cppStyle: CommentBlock =
    new CommentBlock("", "//", "")

  val twirlStyle: CommentBlock =
    new CommentBlock("@*", " *", " *@" + System.lineSeparator())

  val twirlBlock: TwirlCommentBlock.type =
    TwirlCommentBlock
}

private object TwirlCommentBlock {

  private final val NL = System.lineSeparator()

  def apply(text: String): String = {
    val maxLineLength = text.lines.map(_.length).max
    def fillLine(line: String) =
      " * " + line + spaces(maxLineLength - line.length) + " *"

    val commentBlock = text.lines.map(fillLine).mkString(NL)
    val firstLine    = "@**" + stars(maxLineLength + 2)
    val lastLine     = " " + firstLine.reverse

    firstLine ++ NL ++ commentBlock ++ NL ++ lastLine ++ NL ++ NL
  }

  private def spaces(count: Int) = " " * count
  private def stars(count: Int)  = "*" * count
}

private final class CommentBlock(blockPrefix: String, linePrefix: String, blockSuffix: String) {

  private val newLine = System.lineSeparator()

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
