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

package de.heikoseeberger.sbtheader.license

import de.heikoseeberger.sbtheader.HeaderPattern
import org.scalatest.{ Matchers, WordSpec }

class BSD2ClauseSpec extends WordSpec with Matchers {

  "apply" should {

    "return the BSD 2 Clause license with the given copyright year and owner" in {
      val (headerPattern, mit) = BSD2Clause("2015", "Heiko Seeberger")
      val expected =
        s"""|/*
            | * Copyright (c) 2015, Heiko Seeberger
            | * All rights reserved.
            | *
            | * Redistribution and use in source and binary forms, with or without modification,
            | * are permitted provided that the following conditions are met:
            | *
            | * 1. Redistributions of source code must retain the above copyright notice, this
            | *    list of conditions and the following disclaimer.
            | *
            | * 2. Redistributions in binary form must reproduce the above copyright notice,
            | *    this list of conditions and the following disclaimer in the documentation
            | *    and/or other materials provided with the distribution.
            | *
            | * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
            | * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
            | * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
            | * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
            | * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
            | * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
            | * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
            | * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
            | * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
            | * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
            | */
            |
            |""".stripMargin

      mit shouldBe expected
      headerPattern shouldBe HeaderPattern.cStyleBlockComment
    }

    "return the BSD 2 Clause license with hash style" in {
      val (headerPattern, mit) = BSD2Clause("2015", "Heiko Seeberger", "#")
      val expected =
        s"""|# Copyright (c) 2015, Heiko Seeberger
            |# All rights reserved.
            |#
            |# Redistribution and use in source and binary forms, with or without modification,
            |# are permitted provided that the following conditions are met:
            |#
            |# 1. Redistributions of source code must retain the above copyright notice, this
            |#    list of conditions and the following disclaimer.
            |#
            |# 2. Redistributions in binary form must reproduce the above copyright notice,
            |#    this list of conditions and the following disclaimer in the documentation
            |#    and/or other materials provided with the distribution.
            |#
            |# THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
            |# ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
            |# WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
            |# DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
            |# ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
            |# (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
            |# LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
            |# ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
            |# (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
            |# SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
            |
            |""".stripMargin

      mit shouldBe expected
      headerPattern shouldBe HeaderPattern.hashLineComment
    }

    "fail when unknown comment style prefix provided" in {
      intercept[IllegalArgumentException] { BSD2Clause("2015", "Heiko Seeberger", "???") }
    }
  }
}
