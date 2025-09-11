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

import java.io.File
import scala.util.matching.Regex

package object sbtheader {

  type Traversable[+A] = scala.collection.immutable.Traversable[A]
  type Iterable[+A]    = scala.collection.immutable.Iterable[A]
  type Seq[+A]         = scala.collection.immutable.Seq[A]
  type IndexedSeq[+A]  = scala.collection.immutable.IndexedSeq[A]

  final implicit class FileOps(val file: File) extends AnyVal {
    def extension: Option[String] =
      file.getName match {
        case FileOps.extensionPattern(ext) => Some(ext)
        case _                             => None
      }
  }

  private object FileOps {
    val extensionPattern: Regex = """.+\.(.+)""".r
  }

  final val NewLine = System.lineSeparator()
}
