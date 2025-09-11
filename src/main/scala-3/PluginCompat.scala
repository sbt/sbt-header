/*
 * Copyright (c) 2015 - 2025, Heiko Seeberger
 * Copyright (c) 2025, sbt plugin contributors
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package sbtheader

import sbt.{ *, given }

object PluginCompat:
  type LicenseRef = License

  def mkLicense(spdxId: String, uri: URI): License = License(spdxId, uri)
  val Apache2                                      = License.Apache2
  val MIT                                          = License.MIT
end PluginCompat
