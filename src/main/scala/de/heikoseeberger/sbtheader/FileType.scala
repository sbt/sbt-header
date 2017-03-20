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

import scala.util.matching.Regex

case class FileType(extension: String, firstLinePattern: Option[Regex] = None)

object FileType {
  def firstLinePattern(firstLinePattern: String): Regex =
    s"""($firstLinePattern(?:\n|(?:\r\n))+)((?:.|\n|(?:\r\n))*)""".r

  val conf  = FileType("conf")
  val java  = FileType("java")
  val scala = FileType("scala")
  val sh    = FileType("sh", Some(firstLinePattern("#!.*")))
  val xml   = FileType("xml", Some(firstLinePattern("<\\?xml.*\\?>")))
}
