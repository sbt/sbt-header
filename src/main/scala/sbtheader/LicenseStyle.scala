/*
 * Copyright (c) 2015 - 2025, Heiko Seeberger
 * Copyright (c) 2025, sbt plugin contributors
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package sbtheader

sealed trait LicenseStyle

object LicenseStyle {
  final case object Detailed   extends LicenseStyle
  final case object SpdxSyntax extends LicenseStyle
}
