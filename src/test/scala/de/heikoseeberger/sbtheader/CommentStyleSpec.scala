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

import de.heikoseeberger.sbtheader.CommentStyle._
import org.scalatest.{ Matchers, WordSpec }

class CommentStyleSpec extends WordSpec with Matchers {

  val licenseText = "License text"

  "CStyleBlockComment" should {

    "create C style comment blocks" in {
      val expected =
        s"""|/*
            | * $licenseText
            | */
            |
            |""".stripMargin

      cStyleBlockComment(licenseText) shouldBe expected
    }

    "not match a singleline comment without a trailing new line" in {
      cStyleBlockComment.pattern.unapplySeq("/* comment */") shouldBe None
    }

    "not match a multiline comment without a trailing new line" in {
      cStyleBlockComment.pattern.unapplySeq(
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
      cStyleBlockComment.pattern.unapplySeq(header) shouldBe Some(List(header, ""))
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
      cStyleBlockComment.pattern.unapplySeq(header + body) shouldBe Some(List(header, body))
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
      cStyleBlockComment.pattern.unapplySeq(header + body) shouldBe Some(List(header, body))
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
      cStyleBlockComment.pattern.unapplySeq(header + body) shouldBe Some(List(header, body))
    }
  }

  "CppStyleLineComment" should {

    "create C++ style line comments" in {
      val expected =
        s"""|// $licenseText
            |
            |""".stripMargin

      cppStyleLineComment(licenseText) shouldBe expected
    }

    "not match a singleline comment without trailing new lines" in {
      cppStyleLineComment.pattern.unapplySeq("// comment") shouldBe None
    }

    "not match a multiline block comment with a single trailing new line" in {
      cppStyleLineComment.pattern.unapplySeq(
        """|// comment/1
           |// comment/2
           |""".stripMargin
      ) shouldBe None
    }

    "match a comment with trailing new lines not followed by a body" in {
      val header = """|// comment
                      |
                      |
                      |""".stripMargin
      cppStyleLineComment.pattern.unapplySeq(header) shouldBe Some(List(header, ""))
    }

    "match a comment with trailing new lines followed by a body" in {
      val header = """|// comment/1
                      |// comment/2
                      |
                      |""".stripMargin
      val body   = """|def foo(bar):
                      |    print(bar)
                      |""".stripMargin
      cppStyleLineComment.pattern.unapplySeq(header + body) shouldBe Some(List(header, body))
    }
  }

  "HashLineComment" should {

    "create hash line comments" in {
      val expected =
        s"""|# $licenseText
            |
            |""".stripMargin

      hashLineComment(licenseText) shouldBe expected
    }

    "not match a singleline comment without trailing new lines" in {
      hashLineComment.pattern.unapplySeq("# comment") shouldBe None
    }

    "not match a multiline block comment with a single trailing new line" in {
      hashLineComment.pattern.unapplySeq(
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
      hashLineComment.pattern.unapplySeq(header) shouldBe Some(List(header, ""))
    }

    "match a comment with trailing new lines followed by a body" in {
      val header = """|# comment/1
                      |# comment/2
                      |
                      |""".stripMargin
      val body   = """|def foo(bar):
                      |    print(bar)
                      |""".stripMargin
      hashLineComment.pattern.unapplySeq(header + body) shouldBe Some(List(header, body))
    }
  }

  "TwirlStyleComment" should {

    "create Twirl style comments" in {
      val expected =
        s"""|@*
            | * $licenseText
            | *@
            |
            |""".stripMargin

      twirlStyleBlockComment(licenseText) shouldBe expected
    }

    "not match a singleline comment without a trailing new line" in {
      twirlStyleBlockComment.pattern.unapplySeq("@* comment *@") shouldBe None
    }

    "not match a multiline comment without a trailing new line" in {
      twirlStyleBlockComment.pattern.unapplySeq(
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
      twirlStyleBlockComment.pattern.unapplySeq(header) shouldBe Some(List(header, ""))
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
      twirlStyleBlockComment.pattern.unapplySeq(header + body) shouldBe Some(List(header, body))
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
      twirlStyleBlockComment.pattern.unapplySeq(header + body) shouldBe Some(List(header, body))
    }
  }

  "TwirlStyleBlockComment" should {

    "create Twirl style block comments" in {
      val expected =
        s"""|@****************
            | * $licenseText *
            | ****************@
            |
            |""".stripMargin

      twirlStyleFramedBlockComment(licenseText) shouldBe expected
    }

    "not match a singleline comment without a trailing new line" in {
      twirlStyleFramedBlockComment.pattern.unapplySeq("@* comment *@") shouldBe None
    }

    "not match a multiline comment without a trailing new line" in {
      twirlStyleFramedBlockComment.pattern.unapplySeq(
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
      twirlStyleFramedBlockComment.pattern.unapplySeq(header) shouldBe Some(List(header, ""))
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
      twirlStyleFramedBlockComment.pattern.unapplySeq(header + body) shouldBe Some(
        List(header, body)
      )
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
      twirlStyleFramedBlockComment.pattern.unapplySeq(header + body) shouldBe Some(
        List(header, body)
      )
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
      twirlStyleFramedBlockComment.pattern.unapplySeq(header + body) shouldBe Some(
        List(header, body)
      )
    }
  }

  "XmlStyleBlockComment" should {

    "create XML style comment blocks" in {
      val expected =
        s"""|<!--
            |   $licenseText
            |-->
            |
            |""".stripMargin

      xmlStyleBlockComment(licenseText) shouldBe expected
    }

    "not match a single line comment without a trailing new line" in {
      cStyleBlockComment.pattern.unapplySeq("<!-- comment -->") shouldBe None
    }

    "not match a multi line comment without a trailing new line" in {
      xmlStyleBlockComment.pattern.unapplySeq(
        """|<!--
           |   comment/1
           |   comment/2
           |-->""".stripMargin
      ) shouldBe None
    }

    "match a comment with trailing new lines not followed by a body" in {
      val header = """|<!-- comment -->
                      |
                      |""".stripMargin
      xmlStyleBlockComment.pattern.unapplySeq(header) shouldBe Some(List(header, ""))
    }

    "match a comment with a trailing new line followed by a body" in {
      val header = """|<!--
                      |   comment/1
                      |   comment/2
                      |-->
                      |""".stripMargin
      val body   = """|<order>
                      |  <item quantity="5" name="Bottle of Beer" />
                      |</order>
                      |""".stripMargin
      xmlStyleBlockComment.pattern.unapplySeq(header + body) shouldBe Some(List(header, body))
    }
  }

  "IdentityCommentCreator" should {

    "not create any prefix in the header" in {
      val expected =
        s"""$licenseText""".stripMargin

      IdentityCommentCreator(licenseText) shouldBe expected
    }

    "create the same license text with new line characters" in {

      val severalLinesLicense =
        """
          | Some random license text
          | with several new line
          | characters.
        """.stripMargin

      val expected = s"""$severalLinesLicense""".stripMargin

      IdentityCommentCreator(severalLinesLicense) shouldBe expected
    }
  }

}
