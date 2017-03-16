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
import de.heikoseeberger.sbtheader.CommentStyle._
import org.scalatest.{ Matchers, WordSpec }

class CommentStyleStec extends WordSpec with Matchers {

  val licenseText = "License text"

  "CStyleBlockComment" should {

    "create C style comment blocks" in {
      val expected =
        s"""|/*
            | * $licenseText
            | */
            |
            |""".stripMargin

      CStyleBlockComment(licenseText) shouldBe expected
    }
  }

  "CppStyleLineComment" should {

    "create C++ style line comments" in {
      val expected =
        s"""|// $licenseText
            |
            |""".stripMargin

      CppStyleLineComment(licenseText) shouldBe expected
    }
  }

  "HashLineComment" should {

    "create hash line comments" in {
      val expected =
        s"""|# $licenseText
            |
            |""".stripMargin

      HashLineComment(licenseText) shouldBe expected
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

      TwirlStyleComment(licenseText) shouldBe expected
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

      TwirlStyleBlockComment(licenseText) shouldBe expected
    }
  }
}
