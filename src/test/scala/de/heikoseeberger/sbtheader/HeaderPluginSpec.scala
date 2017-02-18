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

import de.heikoseeberger.sbtheader.HeaderPlugin.autoImport.HeaderPattern
import org.scalatest.{ Matchers, WordSpec }

class HeaderPluginSpec extends WordSpec with Matchers {

  "HeaderPattern.cStyleBlockComment" should {

    "not match a singleline comment without a trailing new line" in {
      HeaderPattern.cStyleBlockComment.unapplySeq("/* comment */") shouldBe None
    }

    "not match a multiline comment without a trailing new line" in {
      HeaderPattern.cStyleBlockComment.unapplySeq(
        """|/*
           | * comment/1
           | * comment/2
           | */""".stripMargin
      ) shouldBe None
    }

    "match a comment with trailing new lines not followed by a body" in {
      val header = """|/* comment */
                      |
                      |""".stripMargin
      HeaderPattern.cStyleBlockComment.unapplySeq(header) shouldBe Some(List(header, ""))
    }

    "match a comment with a trailing new line followed by a body" in {
      val header = """|/*
                      | * comment/1
                      | * comment/2
                      | */
                      |""".stripMargin
      val body   = """|class Foo {
                    |  val bar = "bar"
                    |}
                    |""".stripMargin
      HeaderPattern.cStyleBlockComment.unapplySeq(header + body) shouldBe Some(List(header, body))
    }

    "match a comment with a trailing new line followed by a body with a ScalaDoc comment" in {
      val header = """|/*
                      | * comment/1
                      | * comment/2
                      | */
                      |""".stripMargin
      val body   = """|/**
                    |  * ScalaDoc for Foo
                    |  */
                    |class Foo {
                    |  val bar = "bar"
                    |}
                    |""".stripMargin
      HeaderPattern.cStyleBlockComment.unapplySeq(header + body) shouldBe Some(List(header, body))
    }

    "match a left indented header with a trailing new line followed by a body with a ScalaDoc comment" in {
      val header = """|/**
                      | * comment/1
                      | * comment/2
                      | */
                      |""".stripMargin
      val body   = """|/**
                    |  * ScalaDoc for Foo
                    |  */
                    |class Foo {
                    |  val bar = "bar"
                    |}
                    |""".stripMargin
      HeaderPattern.cStyleBlockComment.unapplySeq(header + body) shouldBe Some(List(header, body))
    }
  }

  "HeaderPattern.hashLineComment" should {

    "not match a singleline comment without trailing new lines" in {
      HeaderPattern.hashLineComment.unapplySeq("# comment") shouldBe None
    }

    "not match a multiline block comment with a single trailing new line" in {
      HeaderPattern.hashLineComment.unapplySeq(
        """|# comment/1
           |# comment/2
           |""".stripMargin
      ) shouldBe None
    }

    "match a comment with trailing new lines not followed by a body" in {
      val header = """|# comment
                      |
                      |
                      |""".stripMargin
      HeaderPattern.hashLineComment.unapplySeq(header) shouldBe Some(List(header, ""))
    }

    "match a comment with trailing new lines followed by a body" in {
      val header = """|# comment/1
                      |# comment/2
                      |
                      |""".stripMargin
      val body   = """|def foo(bar):
                    |    print(bar)
                    |""".stripMargin
      HeaderPattern.hashLineComment.unapplySeq(header + body) shouldBe Some(List(header, body))
    }
  }

  "HeaderPattern.twirlBlockComment" should {

    "not match a singleline comment without a trailing new line" in {
      HeaderPattern.twirlBlockComment.unapplySeq("@* comment *@") shouldBe None
    }

    "not match a multiline comment without a trailing new line" in {
      HeaderPattern.twirlBlockComment.unapplySeq(
        """|@*************
           | * comment/1 *
           | * comment/2 *
           | *************@""".stripMargin
      ) shouldBe None
    }

    "match a comment with trailing new lines not followed by a body" in {
      val header = """|@* comment *@
                      |
                      |""".stripMargin
      HeaderPattern.twirlBlockComment.unapplySeq(header) shouldBe Some(List(header, ""))
    }

    "match a comment with a trailing new line followed by a body" in {
      val header = """|@*************
                      | * comment/1 *
                      | * comment/2 *
                      | *************@
                      |""".stripMargin
      val body   = """|@(name: String)
                    |
                    |<h1>Hello @name!</h1>
                    |""".stripMargin
      HeaderPattern.twirlBlockComment.unapplySeq(header + body) shouldBe Some(List(header, body))
    }

    "match a comment with a trailing new line followed by a body with a twirl comment" in {
      val header = """|@*************
                      | * comment/1 *
                      | * comment/2 *
                      | *************@
                      |""".stripMargin
      val body   = """|@*****************
                    | * Twirl comment *
                    | *****************@
                    |@(name: String)
                    |
                    |<h1>Hello @name!</h1>
                    |
                    |""".stripMargin
      HeaderPattern.twirlBlockComment.unapplySeq(header + body) shouldBe Some(List(header, body))
    }

    "match a block comment with a trailing new line followed by a body with a twirl comment" in {
      val header = """|@*************
                      | * comment/1 *
                      | * comment/2 *
                      | *************@
                      |""".stripMargin
      val body   = """|@*
                    | * Twirl comment
                    | *@
                    |@(name: String)
                    |
                    |<h1>Hello @name!</h1>
                    |
                    |""".stripMargin
      HeaderPattern.twirlBlockComment.unapplySeq(header + body) shouldBe Some(List(header, body))
    }
  }

  "HeaderPattern.twirlStyleComment" should {

    "not match a singleline comment without a trailing new line" in {
      HeaderPattern.twirlStyleComment.unapplySeq("@* comment *@") shouldBe None
    }

    "not match a multiline comment without a trailing new line" in {
      HeaderPattern.twirlStyleComment.unapplySeq(
        """|@*
           | * comment/1
           | * comment/2
           | *@""".stripMargin
      ) shouldBe None
    }

    "match a comment with trailing new lines not followed by a body" in {
      val header = """|@* comment *@
                      |
                      |""".stripMargin
      HeaderPattern.twirlStyleComment.unapplySeq(header) shouldBe Some(List(header, ""))
    }

    "match a comment with a trailing new line followed by a body" in {
      val header = """|@*
                      | * comment/1
                      | * comment/2
                      | *@
                      |""".stripMargin
      val body   = """|@main("Welcome to Play") {
                    |
                    |    @*
                    |     * Get an `Html` object by calling the built-in Play welcome
                    |     * template and passing a `String` message.
                    |     *@
                    |    @play20.welcome(message, style = "Scala")
                    |
                    |}
                    |""".stripMargin
      HeaderPattern.twirlStyleComment.unapplySeq(header + body) shouldBe Some(List(header, body))
    }

    "match a comment with a trailing new line followed by a body with a twirl block comment" in {
      val header = """|@*
                      | * comment/1
                      | * comment/2
                      | *@
                      |""".stripMargin
      val body   = """|@*************************
                    | * A twirl block comment *
                    | *************************@
                    |@main("Welcome to Play") {
                    |
                    |    @*
                    |     * Get an `Html` object by calling the built-in Play welcome
                    |     * template and passing a `String` message.
                    |     *@
                    |    @play20.welcome(message, style = "Scala")
                    |
                    |}
                    |""".stripMargin
      HeaderPattern.twirlStyleComment.unapplySeq(header + body) shouldBe Some(List(header, body))
    }
  }

}
