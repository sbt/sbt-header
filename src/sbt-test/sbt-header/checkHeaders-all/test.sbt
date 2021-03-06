lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings)
  .settings(headerSettings(IntegrationTest): _*)
  .settings(headerLicense := Some(HeaderLicense.ALv2("2015", "Heiko Seeberger")))
