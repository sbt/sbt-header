val MyTest = config("myTest").extend(Runtime)

lazy val root = (project in file("."))
  .configs(MyTest)
  .settings(inConfig(MyTest)(Defaults.testSettings))
  .settings(headerSettings(MyTest) *)
  .settings(headerLicense := Some(HeaderLicense.ALv2("2015", "Heiko Seeberger")))
