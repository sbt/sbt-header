package de.heikoseeberger.sbtheader

sealed trait LicenseStyle

object LicenseStyle {

  final case object Detailed   extends LicenseStyle
  final case object SpdxSyntax extends LicenseStyle
}
