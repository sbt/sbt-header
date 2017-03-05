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

import de.heikoseeberger.sbtheader.HeaderPlugin.autoImport.HeaderLicense.Apache2_0
import de.heikoseeberger.sbtheader.HeaderPlugin.autoImport.HeaderPattern
import org.scalatest.{ Matchers, WordSpec }

final class Apache2_0Spec extends WordSpec with Matchers {

  "apply" should {

    "return the Apache 2.0 license with the given copyright year and owner" in {
      val (headerPattern, apache2_0) = Apache2_0("2015", "Heiko Seeberger")
      val expected =
        s"""|/*
            | * Copyright 2015 Heiko Seeberger
            | *
            | * Licensed under the Apache License, Version 2.0 (the "License");
            | * you may not use this file except in compliance with the License.
            | * You may obtain a copy of the License at
            | *
            | *     http://www.apache.org/licenses/LICENSE-2.0
            | *
            | * Unless required by applicable law or agreed to in writing, software
            | * distributed under the License is distributed on an "AS IS" BASIS,
            | * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
            | * See the License for the specific language governing permissions and
            | * limitations under the License.
            | */
            |
            |""".stripMargin

      apache2_0 shouldBe expected
      headerPattern shouldBe HeaderPattern.cStyleBlockComment
    }

    "return the Apache 2.0 license with hash style" in {
      val (headerPattern, apache2_0) = Apache2_0("2015", "Heiko Seeberger", HashLineComment)
      val expected =
        """|# Copyright 2015 Heiko Seeberger
           |#
           |# Licensed under the Apache License, Version 2.0 (the "License");
           |# you may not use this file except in compliance with the License.
           |# You may obtain a copy of the License at
           |#
           |#     http://www.apache.org/licenses/LICENSE-2.0
           |#
           |# Unless required by applicable law or agreed to in writing, software
           |# distributed under the License is distributed on an "AS IS" BASIS,
           |# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
           |# See the License for the specific language governing permissions and
           |# limitations under the License.
           |
           |""".stripMargin

      apache2_0 shouldBe expected
      headerPattern shouldBe HeaderPattern.hashLineComment
    }

    "return the Apache 2.0 license with C++ style" in {
      val (headerPattern, apache2_0) = Apache2_0("2015", "Heiko Seeberger", CppStyleLineComment)
      val expected =
        """|// Copyright 2015 Heiko Seeberger
           |//
           |// Licensed under the Apache License, Version 2.0 (the "License");
           |// you may not use this file except in compliance with the License.
           |// You may obtain a copy of the License at
           |//
           |//     http://www.apache.org/licenses/LICENSE-2.0
           |//
           |// Unless required by applicable law or agreed to in writing, software
           |// distributed under the License is distributed on an "AS IS" BASIS,
           |// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
           |// See the License for the specific language governing permissions and
           |// limitations under the License.
           |
           |""".stripMargin

      apache2_0 shouldBe expected
      headerPattern shouldBe HeaderPattern.cppStyleLineComment
    }

    "return the Apache 2.0 license with Twirl block style" in {
      val (headerPattern, apache2_0) = Apache2_0("2015", "Heiko Seeberger", TwirlStyleBlockComment)
      val expected =
        """|@****************************************************************************
           | * Copyright 2015 Heiko Seeberger                                           *
           | *                                                                          *
           | * Licensed under the Apache License, Version 2.0 (the "License");          *
           | * you may not use this file except in compliance with the License.         *
           | * You may obtain a copy of the License at                                  *
           | *                                                                          *
           | *     http://www.apache.org/licenses/LICENSE-2.0                           *
           | *                                                                          *
           | * Unless required by applicable law or agreed to in writing, software      *
           | * distributed under the License is distributed on an "AS IS" BASIS,        *
           | * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. *
           | * See the License for the specific language governing permissions and      *
           | * limitations under the License.                                           *
           | ****************************************************************************@
           |
           |""".stripMargin

      apache2_0 shouldBe expected
      headerPattern shouldBe HeaderPattern.twirlBlockComment
    }

    "return the Apache 2.0 license with Twirl style" in {
      val (headerPattern, apache2_0) = Apache2_0("2015", "Heiko Seeberger", TwirlStyleComment)
      val expected =
        """|@*
           | * Copyright 2015 Heiko Seeberger
           | *
           | * Licensed under the Apache License, Version 2.0 (the "License");
           | * you may not use this file except in compliance with the License.
           | * You may obtain a copy of the License at
           | *
           | *     http://www.apache.org/licenses/LICENSE-2.0
           | *
           | * Unless required by applicable law or agreed to in writing, software
           | * distributed under the License is distributed on an "AS IS" BASIS,
           | * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
           | * See the License for the specific language governing permissions and
           | * limitations under the License.
           | *@
           |
           |""".stripMargin

      apache2_0 shouldBe expected
      headerPattern shouldBe HeaderPattern.twirlStyleComment
    }
  }
}
