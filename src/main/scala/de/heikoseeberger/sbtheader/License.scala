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

import de.heikoseeberger.sbtheader.LicenseStyle.{ Detailed, SpdxSyntax }

sealed trait License {
  def text: String
}

sealed trait SpdxLicense {

  def spdxIdentifier: String

  def apply(yyyy: String, copyrightOwner: String, licenseStyle: LicenseStyle = Detailed): License
}

object License {

  private[sbtheader] val spdxLicenses =
    Vector(
      ALv2,
      MIT,
      MPLv2,
      BSD2Clause,
      BSD3Clause,
      GPLv3OrLater,
      GPLv3Only,
      GPLv3,
      LGPLv3OrLater,
      LGPLv3Only,
      LGPLv3,
      AGPLv3OrLater,
      AGPLv3Only,
      AGPLv3
    )

  private[sbtheader] def buildSpdxSyntax(
      yyyy: String,
      copyrightOwner: String,
      spdxIdentifier: String
  ): String =
    s"""|Copyright $yyyy $copyrightOwner
        |
        |SPDX-License-Identifier: $spdxIdentifier
        |""".stripMargin

  final case object ALv2 extends SpdxLicense {

    override val spdxIdentifier = "Apache-2.0"

    override def apply(yyyy: String, copyrightOwner: String, licenseStyle: LicenseStyle) =
      new ALv2(yyyy, copyrightOwner, licenseStyle)
  }

  final class ALv2(yyyy: String, copyrightOwner: String, licenseStyle: LicenseStyle)
      extends License {

    override val text: String = licenseStyle match {
      case SpdxSyntax =>
        buildSpdxSyntax(yyyy, copyrightOwner, ALv2.spdxIdentifier)

      case Detailed =>
        s"""|Copyright $yyyy $copyrightOwner
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
    }
  }

  final case object MIT extends SpdxLicense {

    override def spdxIdentifier = "MIT"

    override def apply(yyyy: String, copyrightOwner: String, licenseStyle: LicenseStyle) =
      new MIT(yyyy, copyrightOwner, licenseStyle)
  }

  final class MIT(yyyy: String, copyrightOwner: String, licenseStyle: LicenseStyle)
      extends License {

    override val text: String = licenseStyle match {
      case SpdxSyntax =>
        buildSpdxSyntax(yyyy, copyrightOwner, MIT.spdxIdentifier)

      case Detailed =>
        s"""|Copyright (c) $yyyy $copyrightOwner
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
    }
  }

  final case object MPLv2 extends SpdxLicense {

    override def spdxIdentifier = "MPL-2.0"

    override def apply(yyyy: String, copyrightOwner: String, licenseStyle: LicenseStyle) =
      new MPLv2(yyyy, copyrightOwner, licenseStyle)
  }

  final class MPLv2(yyyy: String, copyrightOwner: String, licenseStyle: LicenseStyle)
      extends License {

    override val text: String = licenseStyle match {
      case SpdxSyntax =>
        buildSpdxSyntax(yyyy, copyrightOwner, MPLv2.spdxIdentifier)

      case Detailed =>
        s"""|Copyright (c) $yyyy $copyrightOwner
            |
            |This Source Code Form is subject to the terms of the Mozilla Public
            |License, v. 2.0. If a copy of the MPL was not distributed with this
            |file, You can obtain one at http://mozilla.org/MPL/2.0/.
            |""".stripMargin
    }
  }

  final object MPLv2_NoCopyright extends License {

    override val text: String =
      s"""|This Source Code Form is subject to the terms of the Mozilla Public
          |License, v. 2.0. If a copy of the MPL was not distributed with this
          |file, You can obtain one at http://mozilla.org/MPL/2.0/.
          |""".stripMargin
  }

  final case object BSD2Clause extends SpdxLicense {

    override def spdxIdentifier = "BSD-2-Clause"

    override def apply(yyyy: String, copyrightOwner: String, licenseStyle: LicenseStyle) =
      new BSD2Clause(yyyy, copyrightOwner, licenseStyle)
  }

  final class BSD2Clause(yyyy: String, copyrightOwner: String, licenseStyle: LicenseStyle)
      extends License {

    override val text: String = licenseStyle match {
      case SpdxSyntax =>
        buildSpdxSyntax(yyyy, copyrightOwner, BSD2Clause.spdxIdentifier)

      case Detailed =>
        s"""|Copyright (c) $yyyy, $copyrightOwner
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
    }
  }

  final case object BSD3Clause extends SpdxLicense {

    override def spdxIdentifier = "BSD-3-Clause"

    override def apply(yyyy: String, copyrightOwner: String, licenseStyle: LicenseStyle) =
      new BSD3Clause(yyyy, copyrightOwner, licenseStyle)
  }

  final class BSD3Clause(yyyy: String, copyrightOwner: String, licenseStyle: LicenseStyle)
      extends License {

    override val text: String = licenseStyle match {
      case SpdxSyntax =>
        buildSpdxSyntax(yyyy, copyrightOwner, BSD3Clause.spdxIdentifier)

      case Detailed =>
        s"""|Copyright (c) $yyyy, $copyrightOwner
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
    }
  }

  final case object GPLv3OrLater extends SpdxLicense {

    override def spdxIdentifier = "GPL-3.0-or-later"

    override def apply(yyyy: String, copyrightOwner: String, licenseStyle: LicenseStyle) =
      new GPLv3OrLater(yyyy, copyrightOwner, licenseStyle)

    def detailed(yyyy: String, copyrightOwner: String) =
      s"""|Copyright (C) $yyyy  $copyrightOwner
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
  }

  final class GPLv3OrLater(yyyy: String, copyrightOwner: String, licenseStyle: LicenseStyle)
      extends License {

    override val text: String = licenseStyle match {
      case SpdxSyntax =>
        buildSpdxSyntax(yyyy, copyrightOwner, GPLv3OrLater.spdxIdentifier)

      case Detailed =>
        GPLv3OrLater.detailed(yyyy, copyrightOwner)
    }
  }

  final case object GPLv3Only extends SpdxLicense {

    override def spdxIdentifier = "GPL-3.0-only"

    override def apply(yyyy: String, copyrightOwner: String, licenseStyle: LicenseStyle) =
      new GPLv3Only(yyyy, copyrightOwner, licenseStyle)
  }

  final class GPLv3Only(yyyy: String, copyrightOwner: String, licenseStyle: LicenseStyle)
      extends License {

    override val text: String = licenseStyle match {
      case SpdxSyntax =>
        buildSpdxSyntax(yyyy, copyrightOwner, GPLv3Only.spdxIdentifier)

      case Detailed =>
        s"""|Copyright (C) $yyyy  $copyrightOwner
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
    }
  }

  final case object GPLv3 extends SpdxLicense {

    override def spdxIdentifier = "GPL-3.0"

    override def apply(yyyy: String, copyrightOwner: String, licenseStyle: LicenseStyle) =
      new GPLv3(yyyy, copyrightOwner, licenseStyle)
  }

  final class GPLv3(yyyy: String, copyrightOwner: String, licenseStyle: LicenseStyle)
      extends License {

    override val text: String = licenseStyle match {
      case SpdxSyntax =>
        buildSpdxSyntax(yyyy, copyrightOwner, GPLv3.spdxIdentifier)

      case Detailed =>
        GPLv3OrLater.detailed(yyyy, copyrightOwner)
    }
  }

  final case object LGPLv3OrLater extends SpdxLicense {

    override def spdxIdentifier = "LGPL-3.0-or-later"

    override def apply(yyyy: String, copyrightOwner: String, licenseStyle: LicenseStyle) =
      new LGPLv3OrLater(yyyy, copyrightOwner, licenseStyle)

    def detailed(yyyy: String, copyrightOwner: String) =
      s"""|Copyright (C) $yyyy  $copyrightOwner
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
  }

  final class LGPLv3OrLater(yyyy: String, copyrightOwner: String, licenseStyle: LicenseStyle)
      extends License {
    override val text: String = licenseStyle match {
      case SpdxSyntax =>
        buildSpdxSyntax(yyyy, copyrightOwner, LGPLv3OrLater.spdxIdentifier)

      case Detailed =>
        LGPLv3OrLater.detailed(yyyy, copyrightOwner)
    }
  }

  final case object LGPLv3Only extends SpdxLicense {

    override def spdxIdentifier = "LGPL-3.0-only"

    override def apply(yyyy: String, copyrightOwner: String, licenseStyle: LicenseStyle) =
      new LGPLv3Only(yyyy, copyrightOwner, licenseStyle)
  }

  final class LGPLv3Only(yyyy: String, copyrightOwner: String, licenseStyle: LicenseStyle)
      extends License {
    override val text: String = licenseStyle match {
      case SpdxSyntax =>
        buildSpdxSyntax(yyyy, copyrightOwner, LGPLv3Only.spdxIdentifier)

      case Detailed =>
        s"""|Copyright (C) $yyyy  $copyrightOwner
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
    }
  }

  final case object LGPLv3 extends SpdxLicense {

    override def spdxIdentifier = "LGPL-3.0"

    override def apply(yyyy: String, copyrightOwner: String, licenseStyle: LicenseStyle) =
      new LGPLv3(yyyy, copyrightOwner, licenseStyle)
  }

  final class LGPLv3(yyyy: String, copyrightOwner: String, licenseStyle: LicenseStyle)
      extends License {
    override val text: String = licenseStyle match {
      case SpdxSyntax =>
        buildSpdxSyntax(yyyy, copyrightOwner, LGPLv3.spdxIdentifier)

      case Detailed =>
        LGPLv3OrLater.detailed(yyyy, copyrightOwner)
    }
  }

  final case object AGPLv3OrLater extends SpdxLicense {
    override def spdxIdentifier = "AGPL-3.0-or-later"

    override def apply(yyyy: String, copyrightOwner: String, licenseStyle: LicenseStyle) =
      new AGPLv3OrLater(yyyy, copyrightOwner, licenseStyle)

    def detailed(yyyy: String, copyrightOwner: String) =
      s"""|Copyright (C) $yyyy  $copyrightOwner
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
  }

  final class AGPLv3OrLater(yyyy: String, copyrightOwner: String, licenseStyle: LicenseStyle)
      extends License {

    override val text: String = licenseStyle match {
      case SpdxSyntax =>
        buildSpdxSyntax(yyyy, copyrightOwner, AGPLv3OrLater.spdxIdentifier)

      case Detailed =>
        AGPLv3OrLater.detailed(yyyy, copyrightOwner)
    }
  }

  final case object AGPLv3Only extends SpdxLicense {
    override def spdxIdentifier = "AGPL-3.0-only"

    override def apply(yyyy: String, copyrightOwner: String, licenseStyle: LicenseStyle) =
      new AGPLv3Only(yyyy, copyrightOwner, licenseStyle)
  }

  final class AGPLv3Only(yyyy: String, copyrightOwner: String, licenseStyle: LicenseStyle)
      extends License {

    override val text: String = licenseStyle match {
      case SpdxSyntax =>
        buildSpdxSyntax(yyyy, copyrightOwner, AGPLv3Only.spdxIdentifier)

      case Detailed =>
        s"""|Copyright (C) $yyyy  $copyrightOwner
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
    }
  }

  final case object AGPLv3 extends SpdxLicense {
    override def spdxIdentifier = "AGPL-3.0"

    override def apply(yyyy: String, copyrightOwner: String, licenseStyle: LicenseStyle) =
      new AGPLv3(yyyy, copyrightOwner, licenseStyle)
  }

  final class AGPLv3(yyyy: String, copyrightOwner: String, licenseStyle: LicenseStyle)
      extends License {

    override val text: String = licenseStyle match {
      case SpdxSyntax =>
        buildSpdxSyntax(yyyy, copyrightOwner, AGPLv3.spdxIdentifier)

      case Detailed =>
        AGPLv3OrLater.detailed(yyyy, copyrightOwner)
    }
  }

  final case class Custom(text: String) extends License
}
