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
import sbt.URL

class LicenseDetectionSpec extends WordSpec with Matchers {

  val organizationName = "Heiko Seeberger"
  val yyyy             = 2017
  val startYear        = Some(yyyy)
  val apache: (String, URL) =
    ("Apache-2.0", new URL("https://spdx.org/licenses/Apache-2.0.html#licenseText"))
  val mit: (String, URL) = ("MIT", new URL("https://spdx.org/licenses/MIT.html#licenseText"))

  val licenses: Map[License, (String, URL)] = Map(
    BSD2Clause(yyyy.toString, organizationName) -> ("BSD-2-Clause", new URL(
      "https://spdx.org/licenses/BSD-2-Clause.html#licenseText"
    )),
    BSD3Clause(yyyy.toString, organizationName) -> ("BSD-3-Clause", new URL(
      "https://spdx.org/licenses/BSD-3-Clause.html#licenseText"
    )),
    AGPLv3OrLater(yyyy.toString, organizationName) -> ("AGPL-3.0-or-later", new URL(
      "https://spdx.org/licenses/AGPL-3.0-or-later.html#licenseText"
    )),
    AGPLv3Only(yyyy.toString, organizationName) -> ("AGPL-3.0-only", new URL(
      "https://spdx.org/licenses/AGPL-3.0-only.html#licenseText"
    )),
    AGPLv3(yyyy.toString, organizationName) -> ("AGPL-3.0", new URL(
      "https://spdx.org/licenses/AGPL-3.0.html#licenseText"
    )),
    ALv2(yyyy.toString, organizationName) ->
    apache,
    GPLv3OrLater(yyyy.toString, organizationName) -> ("GPL-3.0-or-later", new URL(
      "https://spdx.org/licenses/GPL-3.0-or-later.html#licenseText"
    )),
    GPLv3Only(yyyy.toString, organizationName) -> ("GPL-3.0-only", new URL(
      "https://spdx.org/licenses/GPL-3.0-only.html#licenseText"
    )),
    GPLv3(yyyy.toString, organizationName) -> ("GPL-3.0", new URL(
      "https://spdx.org/licenses/GPL-3.0.html#licenseText"
    )),
    LGPLv3OrLater(yyyy.toString, organizationName) -> ("LGPL-3.0-or-later", new URL(
      "https://spdx.org/licenses/LGPL-3.0-or-later.html#licenseText"
    )),
    LGPLv3Only(yyyy.toString, organizationName) -> ("LGPL-3.0-only", new URL(
      "https://spdx.org/licenses/LGPL-3.0-only.html#licenseText"
    )),
    LGPLv3(yyyy.toString, organizationName) -> ("LGPL-3.0", new URL(
      "https://spdx.org/licenses/LGPL-3.0.html#licenseText"
    )),
    MIT(yyyy.toString, organizationName) -> mit,
    MPLv2(yyyy.toString, organizationName) -> ("MPL-2.0", new URL(
      "https://spdx.org/licenses/MPL-2.0.html#licenseText"
    ))
  )

  "LicenseDetection" should {
    "not detect a license when sbt licenses is empty" in {
      LicenseDetection(List.empty, organizationName, startYear) shouldBe None
    }

    "not detect a license when sbt licenses contains two licenses" in {
      LicenseDetection(
        List(apache, mit),
        organizationName,
        startYear
      ) shouldBe None
    }

    "not detect a license when startYear is None" in {
      LicenseDetection(
        List(apache),
        organizationName,
        None
      ) shouldBe None
    }

    licenses.foreach {
      case (license, sbtLicense) =>
        s"detect ${license.getClass.getSimpleName} license" in {
          LicenseDetection(List(sbtLicense), organizationName, startYear)
            .map(_.text) shouldBe Some(
            license.text
          )
        }
    }
  }
}
