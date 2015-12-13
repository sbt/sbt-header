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

import org.scalatest.{ Matchers, WordSpec }
import java.io.ByteArrayInputStream

class StubLogger extends sbt.Logger {
  def log(level: sbt.Level.Value, message: => String) = {}
  def success(message: => String) = {}
  def trace(t: => Throwable) = {}
}

class HeaderCreatorSpec extends WordSpec with Matchers {
  import HeaderPlugin._

  "CreatorHeader.getText" should {
    "create a header with crlf and file crlf should produce file crlf" in {
      val fileContent = "this is a file with lf endings\r\n"
      val header = "#this is a header text with lf endings\r\n"

      HeaderCreator(
        HeaderPattern.hashLineComment,
        header,
        new StubLogger,
        new ByteArrayInputStream(fileContent.getBytes)
      ).getText shouldBe Some(header + fileContent)
    }

    "create a header with lf and file lf should produce file lf" in {
      val fileContent = "this is a file with lf endings\n"
      val header = "#this is a header text with lf endings\n"

      HeaderCreator(
        HeaderPattern.hashLineComment,
        header,
        new StubLogger,
        new ByteArrayInputStream(fileContent.getBytes)
      ).getText shouldBe Some(header + fileContent)
    }

    "create a header with lf and file crlf should produce file crlf" in {
      val fileContent = "this is a file with crlf endings\r\n"
      val header = "#this is a header text with lf endings\n"
      val expectedResult = Some(header.replace("\n", "\r\n") + fileContent)

      HeaderCreator(
        HeaderPattern.hashLineComment,
        header,
        new StubLogger,
        new ByteArrayInputStream(fileContent.getBytes)
      ).getText shouldBe expectedResult
    }

    "create a header with crlf and file lf should produce file lf" in {
      val fileContent = "this is a file with lf endings\n"
      val header = "#this is a header text with crlf endings\r\n"
      val expectedResult = Some(header.replace("\r\n", "\n") + fileContent)

      HeaderCreator(
        HeaderPattern.hashLineComment,
        header,
        new StubLogger,
        new ByteArrayInputStream(fileContent.getBytes)
      ).getText shouldBe expectedResult
    }

    "create a header with crlf and file cr should produce file cr" in {
      val fileContent = "this is a file with cr endings\r"
      val header = "#this is a header text with crlf endings\r\n"
      val expectedResult = Some(header.replace("\r\n", "\r") + fileContent)

      HeaderCreator(
        HeaderPattern.hashLineComment,
        header,
        new StubLogger,
        new ByteArrayInputStream(fileContent.getBytes)
      ).getText shouldBe expectedResult
    }

    "it should add as many new lines exists in header" in {
      val fileContent = "this is a file with lf endings\n" +
        "this is a file with lf endings\n" +
        "this is a file with lf endings\n"
      val header = "#this is a header text with multiple lf endings\n\n\n\n"
      val expectedResult = Some(header + fileContent)

      HeaderCreator(
        HeaderPattern.hashLineComment,
        header,
        new StubLogger,
        new ByteArrayInputStream(fileContent.getBytes)
      ).getText shouldBe expectedResult
    }

    //Due to java bug http://bugs.java.com/bugdatabase/view_bug.do?bug_id=8028387
    "it should work with large files" in {
      val fileContent = "this is a file with lf endings\n" * 50
      val header = "#this is a header text with multiple lf endings\n"
      val expectedResult = Some(header + fileContent)

      HeaderCreator(
        HeaderPattern.hashLineComment,
        header,
        new StubLogger,
        new ByteArrayInputStream(fileContent.getBytes)
      ).getText shouldBe expectedResult
    }
  }
}
