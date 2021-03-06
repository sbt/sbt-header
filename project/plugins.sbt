addSbtPlugin("com.dwijnand"      % "sbt-dynver"   % "4.1.1")
addSbtPlugin("com.dwijnand"      % "sbt-travisci" % "1.2.0")
addSbtPlugin("org.foundweekends" % "sbt-bintray"  % "0.6.1")
addSbtPlugin("org.scalameta"     % "sbt-scalafmt" % "2.4.2")

libraryDependencies ++= Seq(
  "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value,
)

// For using the plugin in their own build
unmanagedSourceDirectories in Compile +=
  baseDirectory.in(ThisBuild).value.getParentFile / "src" / "main" / "scala"
