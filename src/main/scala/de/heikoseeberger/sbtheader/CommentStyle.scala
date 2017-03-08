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
  def apply(licenseText: String): (Regex, String)
}

object CommentStyle {

  final case object CStyleBlockComment extends CommentStyle {
    override def apply(licenseText: String): (Regex, String) =
      (cStyleBlockComment, CommentBlock.cStyle(licenseText))
  }

  case object CppStyleLineComment extends CommentStyle {
    override def apply(licenseText: String): (Regex, String) =
      (cppStyleLineComment, CommentBlock.cppStyle(licenseText))
  }

  case object HashLineComment extends CommentStyle {
    override def apply(licenseText: String): (Regex, String) =
      (hashLineComment, CommentBlock.hashLines(licenseText))
  }

  case object TwirlStyleComment extends CommentStyle {
    override def apply(licenseText: String): (Regex, String) =
      (twirlStyleComment, CommentBlock.twirlStyle(licenseText))
  }

  case object TwirlStyleBlockComment extends CommentStyle {
    override def apply(licenseText: String): (Regex, String) =
      (twirlBlockComment, CommentBlock.twirlBlock(licenseText))
  }

}
