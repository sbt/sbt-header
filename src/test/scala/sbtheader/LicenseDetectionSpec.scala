/*
 * Copyright (c) 2015 - 2025, Heiko Seeberger
 * Copyright (c) 2025, sbt plugin contributors
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package sbtheader

import sbtheader.HeaderPlugin.autoImport.HeaderLicense
import HeaderLicense.*
import sbt.url
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import PluginCompat.{ *, given }

class LicenseDetectionSpec extends AnyWordSpec with Matchers {

  val organizationName = "Heiko Seeberger"
  val yyyy             = "2017"
  val startYear        = Some(yyyy)
  val apache           = sbt.License.Apache2
  val mit              = sbt.License.MIT

  val licenses: Map[sbtheader.License, LicenseRef] = Map(
    BSD2Clause(yyyy.toString, organizationName) -> mkLicense(
      "BSD-2-Clause",
      url(
        "https://spdx.org/licenses/BSD-2-Clause.html#licenseText"
      )
    ),
    BSD3Clause(yyyy.toString, organizationName) -> mkLicense(
      "BSD-3-Clause",
      url(
        "https://spdx.org/licenses/BSD-3-Clause.html#licenseText"
      )
    ),
    AGPLv3OrLater(yyyy.toString, organizationName) -> mkLicense(
      "AGPL-3.0-or-later",
      url(
        "https://spdx.org/licenses/AGPL-3.0-or-later.html#licenseText"
      )
    ),
    AGPLv3Only(yyyy.toString, organizationName) -> mkLicense(
      "AGPL-3.0-only",
      url(
        "https://spdx.org/licenses/AGPL-3.0-only.html#licenseText"
      )
    ),
    AGPLv3(yyyy.toString, organizationName) -> mkLicense(
      "AGPL-3.0",
      url(
        "https://spdx.org/licenses/AGPL-3.0.html#licenseText"
      )
    ),
    ALv2(yyyy.toString, organizationName) ->
    apache,
    GPLv3OrLater(yyyy.toString, organizationName) -> mkLicense(
      "GPL-3.0-or-later",
      url(
        "https://spdx.org/licenses/GPL-3.0-or-later.html#licenseText"
      )
    ),
    GPLv3Only(yyyy.toString, organizationName) -> mkLicense(
      "GPL-3.0-only",
      url(
        "https://spdx.org/licenses/GPL-3.0-only.html#licenseText"
      )
    ),
    GPLv3(yyyy.toString, organizationName) -> mkLicense(
      "GPL-3.0",
      url(
        "https://spdx.org/licenses/GPL-3.0.html#licenseText"
      )
    ),
    LGPLv3OrLater(yyyy.toString, organizationName) -> mkLicense(
      "LGPL-3.0-or-later",
      url(
        "https://spdx.org/licenses/LGPL-3.0-or-later.html#licenseText"
      )
    ),
    LGPLv3Only(yyyy.toString, organizationName) -> mkLicense(
      "LGPL-3.0-only",
      url(
        "https://spdx.org/licenses/LGPL-3.0-only.html#licenseText"
      )
    ),
    LGPLv3(yyyy.toString, organizationName) -> mkLicense(
      "LGPL-3.0",
      url(
        "https://spdx.org/licenses/LGPL-3.0.html#licenseText"
      )
    ),
    HeaderLicense.MIT(yyyy.toString, organizationName) -> mit,
    MPLv2(yyyy.toString, organizationName)             -> mkLicense(
      "MPL-2.0",
      url(
        "https://spdx.org/licenses/MPL-2.0.html#licenseText"
      )
    )
  )

  "LicenseDetection" should {
    "not detect a license when sbt licenses is empty" in
    assert(LicenseDetection(List.empty, organizationName, startYear) == None)

    "not detect a license when sbt licenses contains two licenses" in
    assert(
      LicenseDetection(
        List(apache, mit),
        organizationName,
        startYear
      ) == None
    )

    "not detect a license when startYear is None" in
    assert(
      LicenseDetection(
        List(apache),
        organizationName,
        None
      ) == None
    )

    "allow changing license style" in {
      val expected = ALv2(yyyy, organizationName, LicenseStyle.SpdxSyntax)

      assert(
        LicenseDetection(
          List(apache),
          organizationName,
          startYear,
          LicenseStyle.SpdxSyntax
        ).map(_.text) == Some(expected.text)
      )
    }

    "use only the start year even when end year is set and equal" in {
      val expected = ALv2(yyyy, organizationName, LicenseStyle.SpdxSyntax)

      assert(
        LicenseDetection(
          List(apache),
          organizationName,
          startYear.map(_.toInt),
          startYear.map(_.toInt),
          LicenseStyle.SpdxSyntax
        ).map(_.text) == Some(expected.text)
      )
    }

    "detect end year when it is set and larger than start year" in {
      val endYYYY  = yyyy.toInt + 2
      val endYear  = Some(endYYYY)
      val expected = ALv2(s"$yyyy-$endYYYY", organizationName, LicenseStyle.SpdxSyntax)

      assert(
        LicenseDetection(
          List(apache),
          organizationName,
          startYear.map(_.toInt),
          endYear,
          LicenseStyle.SpdxSyntax
        ).map(_.text) == Some(expected.text)
      )
    }

    licenses.foreach { case (license, sbtLicense) =>
      s"detect ${license.getClass.getSimpleName} license" in
      assert(
        LicenseDetection(List(sbtLicense), organizationName, startYear)
          .map(_.text) == Some(
          license.text
        )
      )
    }
  }
}
