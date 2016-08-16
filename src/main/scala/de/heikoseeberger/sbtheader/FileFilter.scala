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

import java.io.File
import java.nio.file.FileSystems

case class FileFilter(excludes: Seq[String]) {

  // adding **/ to the start of the pattern so that users define their ignores relative to the project root
  private val matchers = excludes.map(p => FileSystems.getDefault.getPathMatcher(s"glob:**/$p"))

  def filter(files: Seq[File]): Seq[File] = {
    files.filterNot(f => matchers.exists(_.matches(f.toPath)))
  }
}
