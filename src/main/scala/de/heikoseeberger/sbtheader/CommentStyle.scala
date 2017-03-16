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
    override def apply(licenseText: String) =
      CommentBlock.cStyle(licenseText)

    override def pattern: Regex = cStyleBlockComment
  }

  case object CppStyleLineComment extends CommentStyle {
    override def apply(licenseText: String) =
      CommentBlock.cppStyle(licenseText)

    override def pattern: Regex = cppStyleLineComment
  }

  case object HashLineComment extends CommentStyle {
    override def apply(licenseText: String) =
      CommentBlock.hashLines(licenseText)

    override def pattern: Regex = hashLineComment
  }

  case object TwirlStyleComment extends CommentStyle {
    override def apply(licenseText: String) =
      CommentBlock.twirlStyle(licenseText)

    override def pattern: Regex = twirlStyleComment
  }

  case object TwirlStyleBlockComment extends CommentStyle {
    override def apply(licenseText: String) =
      CommentBlock.twirlBlock(licenseText)

    override def pattern: Regex = twirlBlockComment
  }

}
