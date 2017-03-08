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

import de.heikoseeberger.sbtheader.HeaderPlugin.autoImport.HeaderLicense.LGPLv3
import org.scalatest.{ Matchers, WordSpec }

final class LGPLv3Spec extends WordSpec with Matchers {

  "text" should {

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
}
