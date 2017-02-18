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
import org.scalatest.{ Matchers, WordSpec }

final class FileFilterSpec extends WordSpec with Matchers {

  private val emptyFiles = Vector.empty
  private val scalaFiles = Vector(new File("/project/src/main/scala/SomeFile.scala"),
                                  new File("/project/src/main/scala/AnotherFile.scala"))
  private val javaFiles = Vector(new File("/project/src/main/java/SomeFile.java"),
                                 new File("/project/src/main/java/AnotherFile.java"))
  private val mixedFiles = scalaFiles ++ javaFiles

  "Empty FileFilter" should {

    val filter = FileFilter(Vector.empty)

    "not filter empty files" in {
      filter.filter(emptyFiles) shouldBe emptyFiles
    }

    "not filter files" in {
      filter.filter(scalaFiles) shouldBe scalaFiles
    }
  }

  "FilterFilter filtering scala files" should {
    val filter = FileFilter(Vector("**/*.scala"))

    "not filter empty files" in {
      filter.filter(emptyFiles) shouldBe emptyFiles
    }

    "filter all scala files" in {
      filter.filter(scalaFiles) shouldBe Seq.empty
    }

    "not filter java files" in {
      filter.filter(javaFiles) shouldBe javaFiles
    }

    "should only filter scala files from mixed files" in {
      filter.filter(mixedFiles) shouldBe javaFiles
    }
  }

  "FilterFilter filtering the scala directory" should {
    val filter = FileFilter(Vector("**/scala/**"))

    "not filter empty files" in {
      filter.filter(emptyFiles) shouldBe emptyFiles
    }

    "filter all scala files" in {
      filter.filter(scalaFiles) shouldBe Seq.empty
    }

    "not filter java files" in {
      filter.filter(javaFiles) shouldBe javaFiles
    }

    "should only filter scala files from mixed files" in {
      filter.filter(mixedFiles) shouldBe javaFiles
    }
  }

  "FilterFilter filtering a specific scala file" should {
    val filter = FileFilter(Vector("**/SomeFile.scala"))

    "not filter empty files" in {
      filter.filter(emptyFiles) shouldBe emptyFiles
    }

    "filter only filter that file from all scala files" in {
      filter.filter(scalaFiles) shouldBe Vector(
        new File("/project/src/main/scala/AnotherFile.scala")
      )
    }

    "not filter java files" in {
      filter.filter(javaFiles) shouldBe javaFiles
    }

    "should only filter that file form mixed files" in {
      filter.filter(mixedFiles) shouldBe new File("/project/src/main/scala/AnotherFile.scala") +: javaFiles
    }
  }
}
