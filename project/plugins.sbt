addSbtPlugin("com.dwijnand"      % "sbt-dynver"   % "4.0.0")
addSbtPlugin("com.dwijnand"      % "sbt-travisci" % "1.2.0")
addSbtPlugin("de.heikoseeberger" % "sbt-header"   % "5.3.0")
addSbtPlugin("org.foundweekends" % "sbt-bintray"  % "0.5.5")
addSbtPlugin("org.scalameta"     % "sbt-scalafmt" % "2.2.1")

libraryDependencies ++= Seq(
  "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value,
)
