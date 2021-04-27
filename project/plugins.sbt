addSbtPlugin("com.dwijnand"      % "sbt-dynver"   % "4.1.1")
addSbtPlugin("com.dwijnand"      % "sbt-travisci" % "1.2.0")
addSbtPlugin("org.foundweekends" % "sbt-bintray"  % "0.6.1")
addSbtPlugin("org.scalameta"     % "sbt-scalafmt" % "2.4.2")

// For using this plugin in its own build
Compile / unmanagedSourceDirectories +=
  (ThisBuild / baseDirectory).value.getParentFile / "src" / "main" / "scala"
