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

import sbt.URL
import scala.collection.breakOut

private object LicenseDetection {

  private val spdxMapping =
    License.spdxLicenses.map(l => (l.spdxIdentifier, l))(breakOut): Map[String, SpdxLicense]

  def apply(
      licenses: Seq[(String, URL)],
      organizationName: String,
      startYear: Option[Int]
  ): Option[License] = {
    val licenseName = licenses match {
      case (name, _) :: Nil => Some(name)
      case _                => None
    }

    for {
      name    <- licenseName
      license <- spdxMapping.get(name)
      year    <- startYear
    } yield license(year.toString, organizationName)
  }
}
