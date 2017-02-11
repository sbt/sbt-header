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

class LGPLv3Spec extends WordSpec with Matchers {

  "apply" should {

    "return the LGPLv3 license with the given copyright year and owner" in {
      val (headerPattern, lgplv3) = LGPLv3("2015", "Heiko Seeberger")
      val expected =
        s"""|/*
            | * Copyright (C) 2015  Heiko Seeberger
            | *
            | * This program is free software: you can redistribute it and/or modify
            | * it under the terms of the GNU Lesser General Public License as published
            | * by the Free Software Foundation, either version 3 of the License, or
            | * (at your option) any later version.
            | *
            | * This program is distributed in the hope that it will be useful,
            | * but WITHOUT ANY WARRANTY; without even the implied warranty of
            | * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
            | * GNU Lesser General Public License for more details.
            | *
            | * You should have received a copy of the GNU General Lesser Public License
            | * along with this program.  If not, see <http://www.gnu.org/licenses/>.
            | */
            |
            |""".stripMargin

      lgplv3 shouldBe expected
      headerPattern shouldBe HeaderPattern.cStyleBlockComment
    }

    "return the LGPLv3 license with hash style" in {
      val (headerPattern, lgplv3) = LGPLv3("2015", "Heiko Seeberger", "#")
      val expected =
        """|# Copyright (C) 2015  Heiko Seeberger
           |#
           |# This program is free software: you can redistribute it and/or modify
           |# it under the terms of the GNU Lesser General Public License as published
           |# by the Free Software Foundation, either version 3 of the License, or
           |# (at your option) any later version.
           |#
           |# This program is distributed in the hope that it will be useful,
           |# but WITHOUT ANY WARRANTY; without even the implied warranty of
           |# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
           |# GNU Lesser General Public License for more details.
           |#
           |# You should have received a copy of the GNU General Lesser Public License
           |# along with this program.  If not, see <http://www.gnu.org/licenses/>.
           |
           |""".stripMargin

      lgplv3 shouldBe expected
      headerPattern shouldBe HeaderPattern.hashLineComment
    }

    "return the LGPLv3 license with C++ style" in {
      val (headerPattern, lgplv3) = LGPLv3("2015", "Heiko Seeberger", "//")
      val expected =
        """|// Copyright (C) 2015  Heiko Seeberger
           |//
           |// This program is free software: you can redistribute it and/or modify
           |// it under the terms of the GNU Lesser General Public License as published
           |// by the Free Software Foundation, either version 3 of the License, or
           |// (at your option) any later version.
           |//
           |// This program is distributed in the hope that it will be useful,
           |// but WITHOUT ANY WARRANTY; without even the implied warranty of
           |// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
           |// GNU Lesser General Public License for more details.
           |//
           |// You should have received a copy of the GNU General Lesser Public License
           |// along with this program.  If not, see <http://www.gnu.org/licenses/>.
           |
           |""".stripMargin

      lgplv3 shouldBe expected
      headerPattern shouldBe HeaderPattern.cppStyleLineComment
    }

    "return the LGPLv3 license with Twirl style" in {
      val (headerPattern, lgplv3) = LGPLv3("2015", "Heiko Seeberger", "@*")
      val expected =
        """|@****************************************************************************
           | * Copyright (C) 2015  Heiko Seeberger                                      *
           | *                                                                          *
           | * This program is free software: you can redistribute it and/or modify     *
           | * it under the terms of the GNU Lesser General Public License as published *
           | * by the Free Software Foundation, either version 3 of the License, or     *
           | * (at your option) any later version.                                      *
           | *                                                                          *
           | * This program is distributed in the hope that it will be useful,          *
           | * but WITHOUT ANY WARRANTY; without even the implied warranty of           *
           | * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the            *
           | * GNU Lesser General Public License for more details.                      *
           | *                                                                          *
           | * You should have received a copy of the GNU General Lesser Public License *
           | * along with this program.  If not, see <http://www.gnu.org/licenses/>.    *
           | ****************************************************************************@
           |
           |""".stripMargin

      lgplv3 shouldBe expected
      headerPattern shouldBe HeaderPattern.twirlBlockComment
    }

    "fail when unknown comment style prefix provided" in {
      intercept[IllegalArgumentException] { LGPLv3("2015", "Heiko Seeberger", "???") }
    }
  }
}
