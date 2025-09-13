/*
 * Copyright (c) 2015 - 2025, Heiko Seeberger
 * Copyright (c) 2025, sbt plugin contributors
 *
 * SPDX-License-Identifier: Apache-2.0
 */

import java.io.File
import scala.util.matching.Regex

package object sbtheader {

  type Iterable[+A]   = scala.collection.immutable.Iterable[A]
  type Seq[+A]        = scala.collection.immutable.Seq[A]
  type IndexedSeq[+A] = scala.collection.immutable.IndexedSeq[A]

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
