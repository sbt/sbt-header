/*
 * Copyright (c) 2015 - 2025, Heiko Seeberger
 * Copyright (c) 2025, sbt plugin contributors
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package sbtheader

import sbt.URL

object LicenseDetection:

  private val spdxMapping: Map[String, SpdxLicense] =
    License.spdxLicenses.iterator.map(l => (l.spdxIdentifier, l)).toMap

  def apply(
      licenses: Seq[sbt.License],
      organizationName: String,
      startYear: Option[String],
      licenseStyle: LicenseStyle = LicenseStyle.Detailed
  ): Option[License] =
    apply(licenses, organizationName, startYear.map(_.toInt), None, licenseStyle)

  def apply(
      licenses: Seq[sbt.License],
      organizationName: String,
      startYear: Option[Int],
      endYear: Option[Int],
      licenseStyle: LicenseStyle
  ): Option[License] =
    val spdxIds =
      licenses match
        case l :: Nil => Some(l.spdxId)
        case _        => None
    for
      spdxId  <- spdxIds
      license <- spdxMapping.get(spdxId)
      year    <- combineYears(startYear, endYear)
    yield license(year, organizationName, licenseStyle)

  private def combineYears(startYear: Option[Int], endYear: Option[Int]): Option[String] =
    (startYear, endYear) match
      case (Some(start), Some(end)) if start < end => Some(s"$start-$end")
      case (Some(start), _)                        => Some(start.toString)
      case (None, _)                               => None
end LicenseDetection
