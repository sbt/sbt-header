/*
 * Copyright (c) 2015 - 2025, Heiko Seeberger
 * Copyright (c) 2025, sbt plugin contributors
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package sbtheader

import java.io.ByteArrayInputStream

import sbt.Logger
import sbtheader.HeaderPlugin.autoImport.HeaderCommentStyle.hashLineComment
import sbtheader.HeaderPlugin.autoImport.HeaderLicense.Custom

import scala.util.matching.Regex
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

final class StubLogger extends Logger {
  override def log(level: sbt.Level.Value, message: => String): Unit = ()
  override def success(message: => String): Unit                     = ()
  override def trace(t: => Throwable): Unit                          = ()
}

final class HeaderCreatorSpec extends AnyWordSpec with Matchers {

  "HeaderCreator" when {

    "given a file with crlf line endings" should {

      val fileContent = "this is a file with crlf endings\r\n"

      "produce a file with crlf line endings from a header with crlf line endings" in {
        val licenseText = "this is a header text with crlf endings\r\n"
        val header      = hashLineComment(licenseText)

        createHeader(fileContent, licenseText) shouldBe Some(
          header.replace(NewLine, "\r\n") + fileContent
        )
      }

      "produce a file with crlf line endings from a header with lf line endings" in {
        val licenseText = "this is a header text with lf endings\n"
        val header      = hashLineComment(licenseText)

        createHeader(fileContent, licenseText) shouldBe Some(
          header.replace(NewLine, "\r\n") + fileContent
        )
      }
    }

    "given a file with lf line endings" should {

      val fileContent = "this is a file with lf endings\n"

      "produce a file with lf line endings from a header with lf line endings" in {
        val licenseText = "this is a header text with lf endings\n"
        val header      = hashLineComment(licenseText)

        createHeader(fileContent, licenseText) shouldBe Some(
          header.replace(NewLine, "\n") + fileContent
        )
      }

      "produce a file with lf line endings from a header with crlf line endings" in {
        val licenseText = "this is a header text with crlf endings\r\n"
        val header      = hashLineComment(licenseText)

        createHeader(fileContent, licenseText) shouldBe Some(
          header.replace(NewLine, "\n") + fileContent
        )
      }
    }

    "given a file with cr line endings" should {
      "produce a file with cr line endings from a header with crlf line endings" in {
        val fileContent = "this is a file with cr endings\r"
        val licenseText = "this is a header text with crlf endings\r\n"
        val header      = hashLineComment(licenseText)

        createHeader(fileContent, licenseText) shouldBe Some(
          header.replace(NewLine, "\r") + fileContent
        )
      }
    }

    "given a header with some line breaks" should {

      "add as many new lines to output" in {
        val fileContent = "this is a file with lf endings\n" +
          "this is a file with lf endings\n" +
          "this is a file with lf endings\n"
        val licenseText = "this is a header text with multiple lf endings\n\n\n\n"
        val header      = hashLineComment(licenseText)

        createHeader(fileContent, licenseText) shouldBe Some(
          header.replace(NewLine, "\n") + fileContent
        )
      }
    }

    // Due to java bug http://bugs.java.com/bugdatabase/view_bug.do?bug_id=8028387
    "given a large file with a lot of lf endings" should {
      "produce a file with lf line endings from a header with lf line endings" in {
        val fileContent = "this is a file with lf endings\n" * 50
        val licenseText = "this is a header text with multiple lf endings\n"
        val header      = hashLineComment(licenseText)

        createHeader(fileContent, licenseText) shouldBe Some(
          header.replace(NewLine, "\n") + fileContent
        )
      }
    }

    "given a file with shebang" should {

      val shebang = "#!/bin/bash" + NewLine
      val script =
        """|echo Hello World
           |exit 0
           |""".stripMargin
      val licenseText = "Copyright 2015 Heiko Seeberger"
      val header      = hashLineComment(licenseText)

      "preserve shebang and add header when header is missing" in {
        val fileContent = shebang + script

        createHeader(fileContent, licenseText) shouldBe Some(shebang + header + script)
      }

      "not touch file when header is present" in {
        val fileContent = shebang + header + script

        createHeader(fileContent, licenseText) shouldBe None
      }
    }

    "given an XML file with XML declaration" should {

      val xmlDeclaration = """<?xml version="1.0" encoding="UTF-8"?>""" + NewLine
      val xmlBody =
        """|<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
           |  <parent>
           |    <groupId>my.group</groupId>
           |    <artifactId>my-parent</artifactId>
           |    <version>42</version>
           |  </parent>
           |  <modelVersion>4.0.0</modelVersion>
           |  <groupId>my.group</groupId>
           |  <artifactId>my-artifact</artifactId>
           |  <version>1.1-SNAPTHO</version>
           |</project>
           |""".stripMargin * 100
      val licenseText = "Copyright 2015 Heiko Seeberger"
      val header      = hashLineComment(licenseText)

      "preserve XML declaration and add header when header is missing" in {
        val fileContent    = xmlDeclaration + xmlBody
        val expectedResult = Some(xmlDeclaration + header + xmlBody)

        createHeader(fileContent, licenseText, FileType.xml) shouldBe expectedResult
      }
    }

    "given a UTF-8 encoded file" should {
      "preserve file encoding" in {
        val fileContent = "this is a file with UTF-8 chars: árvíztűrő ütvefúrógép\n"
        val licenseText = "license with UTF-8 chars $ → €\n"

        createHeader(fileContent, licenseText) shouldBe Some(
          hashLineComment(licenseText).replace(NewLine, "\n") + fileContent
        )
      }
    }

    "given a file with an existing copyright year" should {
      val yearPreservingStyle =
        CommentStyle.cStyleBlockComment.copy(commentCreator = new CommentCreator() {
          val Pattern: Regex = "(?s).*?(\\d{4}(-\\d{4})?).*".r
          def findYear(header: String): Option[String] =
            header match {
              case Pattern(years, _) => Some(years)
              case _                 => None
            }
          override def apply(text: String, existingText: Option[String]): String = {
            val newText = CommentStyle.cStyleBlockComment.commentCreator.apply(text, existingText)
            existingText
              .flatMap(findYear)
              .map(year => newText.replace("2017", year))
              .getOrElse(newText)
          }
        })
      val licenseText = "Copyright 2017 MyCorp, Inc <https://mycorp.com>"

      "allow updating the header retaining the copyright year" in {
        val fileContent = """/*
        | * Copyright 2016 MyCorp http://mycorp.com
        | */
        |This is the file content
        |""".stripMargin

        createHeader(fileContent, licenseText, commentCreator = yearPreservingStyle) shouldBe Some(
          """/*
          | * Copyright 2016 MyCorp, Inc <https://mycorp.com>
          | */
          |
          |This is the file content
          |""".stripMargin
        )
      }
    }

    "given a file without an empty line between the header and the body" should {
      "preserve the file with C style without the empty line" in {
        val fileContent = """/*
          | * Copyright 2017 MyCorp, Inc <https://mycorp.com>
          | */
          |This is the file content
          |""".stripMargin

        createHeader(
          fileContent = fileContent,
          header = "Copyright 2017 MyCorp, Inc <https://mycorp.com>",
          commentCreator = CommentStyle.cStyleBlockComment,
          headerEmptyLine = false
        ) shouldBe None
      }

      "preserve the file with C++ style without the empty line" in {
        val fileContent =
          """// Copyright 2017 MyCorp, Inc <https://mycorp.com>
            |This is the file content
            |""".stripMargin

        createHeader(
          fileContent = fileContent,
          header = "Copyright 2017 MyCorp, Inc <https://mycorp.com>",
          commentCreator = CommentStyle.cppStyleLineComment,
          headerEmptyLine = false
        ) shouldBe None
      }

      "preserve the file with hash style without the empty line" in {
        val fileContent =
          """# Copyright 2017 MyCorp, Inc <https://mycorp.com>
            |This is the file content
            |""".stripMargin

        createHeader(
          fileContent = fileContent,
          header = "Copyright 2017 MyCorp, Inc <https://mycorp.com>",
          commentCreator = CommentStyle.hashLineComment,
          headerEmptyLine = false
        ) shouldBe None
      }
    }
  }

  private def createHeader(
      fileContent: String,
      header: String,
      fileType: FileType = FileType.sh,
      commentCreator: CommentStyle = hashLineComment,
      headerEmptyLine: Boolean = true
  ) =
    HeaderCreator(
      fileType,
      commentCreator,
      Custom(header),
      headerEmptyLine,
      new StubLogger,
      new ByteArrayInputStream(fileContent.getBytes)
    ).createText
}
