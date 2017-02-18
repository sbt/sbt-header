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

class MITSpec extends WordSpec with Matchers {

  "apply" should {

    "return the MIT license with the given copyright year and owner" in {
      val (headerPattern, mit) = MIT("2015", "Heiko Seeberger")
      val expected =
        s"""|/*
            | * Copyright (c) 2015 Heiko Seeberger
            | *
            | * Permission is hereby granted, free of charge, to any person obtaining a copy of
            | * this software and associated documentation files (the "Software"), to deal in
            | * the Software without restriction, including without limitation the rights to
            | * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
            | * the Software, and to permit persons to whom the Software is furnished to do so,
            | * subject to the following conditions:
            | *
            | * The above copyright notice and this permission notice shall be included in all
            | * copies or substantial portions of the Software.
            | *
            | * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
            | * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
            | * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
            | * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
            | * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
            | * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
            | */
            |
            |""".stripMargin

      mit shouldBe expected
      headerPattern shouldBe HeaderPattern.cStyleBlockComment
    }

    "return the MIT license with hash style" in {
      val (headerPattern, mit) = MIT("2015", "Heiko Seeberger", "#")
      val expected =
        s"""|# Copyright (c) 2015 Heiko Seeberger
            |#
            |# Permission is hereby granted, free of charge, to any person obtaining a copy of
            |# this software and associated documentation files (the "Software"), to deal in
            |# the Software without restriction, including without limitation the rights to
            |# use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
            |# the Software, and to permit persons to whom the Software is furnished to do so,
            |# subject to the following conditions:
            |#
            |# The above copyright notice and this permission notice shall be included in all
            |# copies or substantial portions of the Software.
            |#
            |# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
            |# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
            |# FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
            |# COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
            |# IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
            |# CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
            |
            |""".stripMargin

      mit shouldBe expected
      headerPattern shouldBe HeaderPattern.hashLineComment
    }

    "return the MIT license with C++ style" in {
      val (headerPattern, mit) = MIT("2015", "Heiko Seeberger", "//")
      val expected =
        s"""|// Copyright (c) 2015 Heiko Seeberger
            |//
            |// Permission is hereby granted, free of charge, to any person obtaining a copy of
            |// this software and associated documentation files (the "Software"), to deal in
            |// the Software without restriction, including without limitation the rights to
            |// use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
            |// the Software, and to permit persons to whom the Software is furnished to do so,
            |// subject to the following conditions:
            |//
            |// The above copyright notice and this permission notice shall be included in all
            |// copies or substantial portions of the Software.
            |//
            |// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
            |// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
            |// FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
            |// COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
            |// IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
            |// CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
            |
            |""".stripMargin

      mit shouldBe expected
      headerPattern shouldBe HeaderPattern.cppStyleLineComment
    }

    "return the MIT license with Twirl block style" in {
      val (headerPattern, mit) = MIT("2015", "Heiko Seeberger", "@**")
      val expected =
        s"""|@************************************************************************************
            | * Copyright (c) 2015 Heiko Seeberger                                               *
            | *                                                                                  *
            | * Permission is hereby granted, free of charge, to any person obtaining a copy of  *
            | * this software and associated documentation files (the "Software"), to deal in    *
            | * the Software without restriction, including without limitation the rights to     *
            | * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of *
            | * the Software, and to permit persons to whom the Software is furnished to do so,  *
            | * subject to the following conditions:                                             *
            | *                                                                                  *
            | * The above copyright notice and this permission notice shall be included in all   *
            | * copies or substantial portions of the Software.                                  *
            | *                                                                                  *
            | * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR       *
            | * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS *
            | * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR   *
            | * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER   *
            | * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN          *
            | * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.       *
            | ************************************************************************************@
            |
            |""".stripMargin

      mit shouldBe expected
      headerPattern shouldBe HeaderPattern.twirlBlockComment
    }

    "return the MIT license with Twirl style" in {
      val (headerPattern, mit) = MIT("2015", "Heiko Seeberger", "@*")
      val expected =
        s"""|@*
            | * Copyright (c) 2015 Heiko Seeberger
            | *
            | * Permission is hereby granted, free of charge, to any person obtaining a copy of
            | * this software and associated documentation files (the "Software"), to deal in
            | * the Software without restriction, including without limitation the rights to
            | * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
            | * the Software, and to permit persons to whom the Software is furnished to do so,
            | * subject to the following conditions:
            | *
            | * The above copyright notice and this permission notice shall be included in all
            | * copies or substantial portions of the Software.
            | *
            | * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
            | * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
            | * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
            | * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
            | * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
            | * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
            | *@
            |
            |""".stripMargin

      mit shouldBe expected
      headerPattern shouldBe HeaderPattern.twirlStyleComment
    }

    "fail when unknown comment style prefix provided" in {
      intercept[IllegalArgumentException] { MIT("2015", "Heiko Seeberger", "???") }
    }
  }
}
