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

import de.heikoseeberger.sbtheader.HeaderPlugin.autoImport.HeaderLicense.{
  MPLv2,
  MPLv2_NoCopyright
}
import org.scalatest.{ Matchers, WordSpec }

final class MPLv2NoCopyrigthSpec extends WordSpec with Matchers {

  "text" should {

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
