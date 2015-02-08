/*
 * Copyright 2015 Heiko Seeberger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.heikoseeberger.sbtheader

import org.scalatest.{ Matchers, WordSpec }

class SbtHeaderSpec extends WordSpec with Matchers {

  import SbtHeader.autoImport._

  "HeaderPattern.blockComment" should {

    "not match a singleline block comment without a trailing new line" in {
      HeaderPattern.blockComment.unapplySeq("/* comment */") shouldBe None
    }

    "not match a multiline block comment without a trailing new line" in {
      HeaderPattern.blockComment.unapplySeq(
        """|/*
           | * comment/1
           | * comment/2
           | */""".stripMargin
      ) shouldBe None
    }

    "match a block comment with trailing new lines not followed by a body" in {
      val header = """|/* comment */
                      |
                      |""".stripMargin
      HeaderPattern.blockComment.unapplySeq(header) shouldBe Some(List(header, ""))
    }

    "match a block comment with a trailing new line followed by a body" in {
      val header = """|/*
                      | * comment/1
                      | * comment/2
                      | */
                      |""".stripMargin
      val body = """|class Foo {
                    |  val bar = "bar"
                    |}
                    |""".stripMargin
      HeaderPattern.blockComment.unapplySeq(header + body) shouldBe Some(List(header, body))
    }

    "match a block comment with a trailing new line followed by a body with a ScalaDoc comment" in {
      val header = """|/*
                      | * comment/1
                      | * comment/2
                      | */
                      |""".stripMargin
      val body = """|/**
                    |  * ScalaDoc for Foo
                    |  */
                    |class Foo {
                    |  val bar = "bar"
                    |}
                    |""".stripMargin
      HeaderPattern.blockComment.unapplySeq(header + body) shouldBe Some(List(header, body))
    }
  }
}
