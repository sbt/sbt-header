# sbt-header #

sbt-header is an [sbt](http://www.scala-sbt.org) plugin for creating file headers, e.g. copyright headers.

Attention:
- sbt-header is still under development and should be considered experimental
- There are still some features missing, e.g. removal of "wrong" headers

## Usage

In order to add the sbt-header plugin to your build, just add the below line to `project/plugins.sbt`:

```
addSbtPlugin("de.heikoseeberger" % "sbt-header" % "0.1.0")
```

You have to define which source files should have which header: sbt-header uses a mapping from file extension
to header for that purpose, specified with the `headers` setting. Add a line like the example below to your `build.sbt`:

```
headers := Map("scala" -> f"/* This is my header ;-)%n")
```

In order to create the file headers, execute the `createHeaders` task:

```
> createHeaders
[info] Headers created for 2 files:
[info]   /Users/heiko/projects/sbt-header/sbt-header-test/test.scala
[info]   /Users/heiko/projects/sbt-header/sbt-header-test/test2.scala
```

## Contribution policy ##

Contributions via GitHub pull requests are gladly accepted from their original author. Along with any pull requests, please state that the contribution is your original work and that you license the work to the project under the project's open source license. Whether or not you state this explicitly, by submitting any copyrighted material via pull request, email, or other means you agree to license the material under the project's open source license and warrant that you have the legal authority to do so.

## License ##

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
