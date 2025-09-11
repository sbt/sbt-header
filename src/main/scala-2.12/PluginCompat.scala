/*
 * Copyright (c) 2015 - 2025, Heiko Seeberger
 * Copyright (c) 2025, sbt plugin contributors
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package sbtheader

import sbt.{ *, given }

object PluginCompat {
  type LicenseRef = (String, URL)

  def mkLicense(spdxId: String, uri: URL) = (spdxId, uri)

  // This adds `Def.uncached(...)`
  implicit class DefOp(singleton: Def.type) {
    def uncached[A1](a: A1): A1 = a
  }
}
