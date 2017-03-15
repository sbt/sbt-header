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

import de.heikoseeberger.sbtheader.CommentStyle.HashLineComment
import de.heikoseeberger.sbtheader.HeaderPlugin.autoImport.HeaderCommentStyleMapping
import de.heikoseeberger.sbtheader.HeaderPlugin.autoImport.HeaderLicense.ALv2
import de.heikoseeberger.sbtheader.HeaderPlugin.autoImport.HeaderPattern.{
  cStyleBlockComment,
  hashLineComment
}
import org.scalatest.{ Matchers, WordSpec }

final class HeaderCommentStyleMappingSpec extends WordSpec with Matchers {

  "Default HeaderCommentStyleMapping" should {
    val mapping = HeaderCommentStyleMapping.createFrom(ALv2("2016", "John Doe"))

    "contain a mapping from java files to C style block comments" in {
      mapping should contain key "java"
      mapping("java")._1 shouldBe cStyleBlockComment
    }

    "contain a mapping from scala files to C style block comments" in {
      mapping should contain key "scala"
      mapping("scala")._1 shouldBe cStyleBlockComment
    }
  }

  "Custom HeaderCommentStyleMapping" should {
    val mapping = HeaderCommentStyleMapping.createFrom(
      ALv2("2016", "John Doe"),
      Vector("conf" -> HashLineComment, "properties" -> HashLineComment, "yml" -> HashLineComment)
    )

    "map conf files to hash line comments" in {
      mapping should contain key "conf"
      mapping("conf")._1 shouldBe hashLineComment
    }

    "map properties files to hash line comments" in {
      mapping should contain key "properties"
      mapping("properties")._1 shouldBe hashLineComment
    }

    "map yml files to hash line comments" in {
      mapping should contain key "yml"
      mapping("yml")._1 shouldBe hashLineComment
    }
  }
}
