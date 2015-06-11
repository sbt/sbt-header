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

package de.heikoseeberger

import java.io.File
import scala.util.matching.Regex

package object sbtheader {

  // format: OFF
  val Traversable = scala.collection.immutable.Traversable
  type Traversable[+A] = scala.collection.immutable.Traversable[A]

  val Iterable = scala.collection.immutable.Iterable
  type Iterable[+A] = scala.collection.immutable.Iterable[A]

  val Seq = scala.collection.immutable.Seq
  type Seq[+A] = scala.collection.immutable.Seq[A]

  val IndexedSeq = scala.collection.immutable.IndexedSeq
  type IndexedSeq[+A] = scala.collection.immutable.IndexedSeq[A]
  // format: ON

  object FileOps {
    val extensionPattern: Regex = """.+\.(.+)""".r
  }

  implicit class FileOps(val file: File) extends AnyVal {
    def extension: Option[String] =
      file.getName match {
        case FileOps.extensionPattern(ext) => Some(ext)
        case _                             => None
      }
  }

  val newLine: String = System.getProperty("line.separator")
}
