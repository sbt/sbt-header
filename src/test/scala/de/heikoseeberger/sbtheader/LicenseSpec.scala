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

import de.heikoseeberger.sbtheader.HeaderPlugin.autoImport.HeaderLicense._
import org.scalatest.{ Matchers, WordSpec }

class licenseSpec extends WordSpec with Matchers {

  "AGPLv3OrLater" should {

    "contain the AGPLv3 or later license with the given copyright year and owner" in {
      val agplv3 = AGPLv3OrLater("2020", "Edward Samson").text
      val expected =
        s"""|Copyright (C) 2020  Edward Samson
            |
            |This program is free software: you can redistribute it and/or modify
            |it under the terms of the GNU Affero General Public License as
            |published by the Free Software Foundation, either version 3 of the
            |License, or (at your option) any later version.
            |
            |This program is distributed in the hope that it will be useful,
            |but WITHOUT ANY WARRANTY; without even the implied warranty of
            |MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
            |GNU Affero General Public License for more details.
            |
            |You should have received a copy of the GNU Affero General Public License
            |along with this program.  If not, see <http://www.gnu.org/licenses/>.
            |""".stripMargin

      agplv3 shouldBe expected
    }
  }

  "AGPLv3Only" should {

    "contain the AGPLv3 only license with the given copyright year and owner" in {
      val agplv3 = AGPLv3Only("2020", "Edward Samson").text
      val expected =
        s"""|Copyright (C) 2020  Edward Samson
            |
            |This program is free software: you can redistribute it and/or modify
            |it under the terms of the GNU Affero General Public License as
            |published by the Free Software Foundation, version 3.
            |
            |This program is distributed in the hope that it will be useful,
            |but WITHOUT ANY WARRANTY; without even the implied warranty of
            |MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
            |GNU Affero General Public License for more details.
            |
            |You should have received a copy of the GNU Affero General Public License
            |along with this program.  If not, see <http://www.gnu.org/licenses/>.
            |""".stripMargin

      agplv3 shouldBe expected
    }
  }

  "AGPLv3" should {

    "contain the AGPLv3 license with the given copyright year and owner" in {
      val agplv3 = AGPLv3("2015", "Heiko Seeberger").text
      val expected =
        s"""|Copyright (C) 2015  Heiko Seeberger
            |
            |This program is free software: you can redistribute it and/or modify
            |it under the terms of the GNU Affero General Public License as
            |published by the Free Software Foundation, either version 3 of the
            |License, or (at your option) any later version.
            |
            |This program is distributed in the hope that it will be useful,
            |but WITHOUT ANY WARRANTY; without even the implied warranty of
            |MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
            |GNU Affero General Public License for more details.
            |
            |You should have received a copy of the GNU Affero General Public License
            |along with this program.  If not, see <http://www.gnu.org/licenses/>.
            |""".stripMargin

      agplv3 shouldBe expected
    }
  }

  "Apache 2.0" should {

    "contain the Apache 2.0 license with the given copyright year and owner" in {
      val alv2 = ALv2("2015", "Heiko Seeberger").text
      val expected =
        s"""|Copyright 2015 Heiko Seeberger
            |
            |Licensed under the Apache License, Version 2.0 (the "License");
            |you may not use this file except in compliance with the License.
            |You may obtain a copy of the License at
            |
            |    http://www.apache.org/licenses/LICENSE-2.0
            |
            |Unless required by applicable law or agreed to in writing, software
            |distributed under the License is distributed on an "AS IS" BASIS,
            |WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
            |See the License for the specific language governing permissions and
            |limitations under the License.
            |""".stripMargin

      alv2 shouldBe expected
    }
  }

  "BSD 2 Clause" should {

    "contain the BSD 2 Clause license with the given copyright year and owner" in {
      val bsd2 = BSD2Clause("2015", "Heiko Seeberger").text
      val expected =
        s"""|Copyright (c) 2015, Heiko Seeberger
            |All rights reserved.
            |
            |Redistribution and use in source and binary forms, with or without modification,
            |are permitted provided that the following conditions are met:
            |
            |1. Redistributions of source code must retain the above copyright notice, this
            |   list of conditions and the following disclaimer.
            |
            |2. Redistributions in binary form must reproduce the above copyright notice,
            |   this list of conditions and the following disclaimer in the documentation
            |   and/or other materials provided with the distribution.
            |
            |THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
            |ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
            |WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
            |DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
            |ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
            |(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
            |LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
            |ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
            |(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
            |SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
            |""".stripMargin

      bsd2 shouldBe expected
    }
  }

  "BSD 3 Clause" should {

    "contain the BSD 3 Clause license with the given copyright year and owner" in {
      val bsd3 = BSD3Clause("2015", "Heiko Seeberger").text
      val expected =
        s"""|Copyright (c) 2015, Heiko Seeberger
            |All rights reserved.
            |
            |Redistribution and use in source and binary forms, with or without modification,
            |are permitted provided that the following conditions are met:
            |
            |1. Redistributions of source code must retain the above copyright notice, this
            |   list of conditions and the following disclaimer.
            |
            |2. Redistributions in binary form must reproduce the above copyright notice,
            |   this list of conditions and the following disclaimer in the documentation
            |   and/or other materials provided with the distribution.
            |
            |3. Neither the name of the copyright holder nor the names of its contributors
            |   may be used to endorse or promote products derived from this software without
            |   specific prior written permission.
            |
            |THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
            |ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
            |WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
            |DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
            |ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
            |(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
            |LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
            |ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
            |(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
            |SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
            |""".stripMargin

      bsd3 shouldBe expected
    }
  }

  "GPLv3OrLater" should {

    "contain the GPLv3 or later license with the given copyright year and owner" in {
      val gplv3 = GPLv3OrLater("2020", "Edward Samson").text
      val expected =
        s"""|Copyright (C) 2020  Edward Samson
            |
            |This program is free software: you can redistribute it and/or modify
            |it under the terms of the GNU General Public License as published by
            |the Free Software Foundation, either version 3 of the License, or
            |(at your option) any later version.
            |
            |This program is distributed in the hope that it will be useful,
            |but WITHOUT ANY WARRANTY; without even the implied warranty of
            |MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
            |GNU General Public License for more details.
            |
            |You should have received a copy of the GNU General Public License
            |along with this program.  If not, see <http://www.gnu.org/licenses/>.
            |""".stripMargin

      gplv3 shouldBe expected
    }
  }

  "GPLv3Only" should {

    "contain the GPLv3 only license with the given copyright year and owner" in {
      val gplv3 = GPLv3Only("2020", "Edward Samson").text
      val expected =
        s"""|Copyright (C) 2020  Edward Samson
            |
            |This program is free software: you can redistribute it and/or modify
            |it under the terms of the GNU General Public License as published by
            |the Free Software Foundation, version 3.
            |
            |This program is distributed in the hope that it will be useful,
            |but WITHOUT ANY WARRANTY; without even the implied warranty of
            |MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
            |GNU General Public License for more details.
            |
            |You should have received a copy of the GNU General Public License
            |along with this program.  If not, see <http://www.gnu.org/licenses/>.
            |""".stripMargin

      gplv3 shouldBe expected
    }
  }

  "GPLv3" should {

    "contain the GPLv3 license with the given copyright year and owner" in {
      val gplv3 = GPLv3("2015", "Heiko Seeberger").text
      val expected =
        s"""|Copyright (C) 2015  Heiko Seeberger
            |
            |This program is free software: you can redistribute it and/or modify
            |it under the terms of the GNU General Public License as published by
            |the Free Software Foundation, either version 3 of the License, or
            |(at your option) any later version.
            |
            |This program is distributed in the hope that it will be useful,
            |but WITHOUT ANY WARRANTY; without even the implied warranty of
            |MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
            |GNU General Public License for more details.
            |
            |You should have received a copy of the GNU General Public License
            |along with this program.  If not, see <http://www.gnu.org/licenses/>.
            |""".stripMargin

      gplv3 shouldBe expected
    }
  }

  "LGPLv3OrLater" should {

    "contain the LGPLv3 or later license with the given copyright year and owner" in {
      val lgplv3 = LGPLv3OrLater("2020", "Edward Samson").text
      val expected =
        s"""|Copyright (C) 2020  Edward Samson
            |
            |This program is free software: you can redistribute it and/or modify
            |it under the terms of the GNU Lesser General Public License as published
            |by the Free Software Foundation, either version 3 of the License, or
            |(at your option) any later version.
            |
            |This program is distributed in the hope that it will be useful,
            |but WITHOUT ANY WARRANTY; without even the implied warranty of
            |MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
            |GNU Lesser General Public License for more details.
            |
            |You should have received a copy of the GNU General Lesser Public License
            |along with this program.  If not, see <http://www.gnu.org/licenses/>.
            |""".stripMargin

      lgplv3 shouldBe expected
    }
  }

  "LGPLv3Only" should {

    "contain the LGPLv3 only license with the given copyright year and owner" in {
      val lgplv3 = LGPLv3Only("2020", "Edward Samson").text
      val expected =
        s"""|Copyright (C) 2020  Edward Samson
            |
            |This program is free software: you can redistribute it and/or modify
            |it under the terms of the GNU Lesser General Public License as published
            |by the Free Software Foundation, version 3.
            |
            |This program is distributed in the hope that it will be useful,
            |but WITHOUT ANY WARRANTY; without even the implied warranty of
            |MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
            |GNU Lesser General Public License for more details.
            |
            |You should have received a copy of the GNU General Lesser Public License
            |along with this program.  If not, see <http://www.gnu.org/licenses/>.
            |""".stripMargin

      lgplv3 shouldBe expected
    }
  }

  "LGPLv3" should {

    "contain the LGPLv3 license with the given copyright year and owner" in {
      val lgplv3 = LGPLv3("2015", "Heiko Seeberger").text
      val expected =
        s"""|Copyright (C) 2015  Heiko Seeberger
            |
            |This program is free software: you can redistribute it and/or modify
            |it under the terms of the GNU Lesser General Public License as published
            |by the Free Software Foundation, either version 3 of the License, or
            |(at your option) any later version.
            |
            |This program is distributed in the hope that it will be useful,
            |but WITHOUT ANY WARRANTY; without even the implied warranty of
            |MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
            |GNU Lesser General Public License for more details.
            |
            |You should have received a copy of the GNU General Lesser Public License
            |along with this program.  If not, see <http://www.gnu.org/licenses/>.
            |""".stripMargin

      lgplv3 shouldBe expected
    }
  }

  "MIT" should {

    "contain the MIT license with the given copyright year and owner" in {
      val mit = MIT("2015", "Heiko Seeberger").text
      val expected =
        s"""|Copyright (c) 2015 Heiko Seeberger
            |
            |Permission is hereby granted, free of charge, to any person obtaining a copy of
            |this software and associated documentation files (the "Software"), to deal in
            |the Software without restriction, including without limitation the rights to
            |use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
            |the Software, and to permit persons to whom the Software is furnished to do so,
            |subject to the following conditions:
            |
            |The above copyright notice and this permission notice shall be included in all
            |copies or substantial portions of the Software.
            |
            |THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
            |IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
            |FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
            |COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
            |IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
            |CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
            |""".stripMargin

      mit shouldBe expected
    }
  }

  "MPL 2.0" should {

    "contain the MPL 2.0 license with the given copyright year and owner" in {
      val mit = MPLv2("2015", "Heiko Seeberger").text
      val expected =
        s"""|Copyright (c) 2015 Heiko Seeberger
            |
            |This Source Code Form is subject to the terms of the Mozilla Public
            |License, v. 2.0. If a copy of the MPL was not distributed with this
            |file, You can obtain one at http://mozilla.org/MPL/2.0/.
            |""".stripMargin

      mit shouldBe expected
    }
  }

  "MPL 2.0 license without copyright" should {

    "contain the MPL 2.0 license without copyright" in {
      val mit = MPLv2_NoCopyright.text
      val expected =
        s"""|This Source Code Form is subject to the terms of the Mozilla Public
            |License, v. 2.0. If a copy of the MPL was not distributed with this
            |file, You can obtain one at http://mozilla.org/MPL/2.0/.
            |""".stripMargin

      mit shouldBe expected
    }
  }
}
