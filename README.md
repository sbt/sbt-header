# sbt-header #

[![Build Status](https://travis-ci.org/sbt/sbt-header.svg?branch=master)](https://travis-ci.org/sbt/sbt-header)
[![Build status](https://ci.appveyor.com/api/projects/status/bycajw36t0fie2s0/branch/master?svg=true)](https://ci.appveyor.com/project/britter/sbt-header/branch/master)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/2cd1ffe963484f45902e8ca2a72276c6)](https://www.codacy.com/app/britter/sbt-header?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=sbt/sbt-header&amp;utm_campaign=Badge_Grade)
[![Download](https://api.bintray.com/packages/hseeberger/sbt-plugins/sbt-header/images/download.svg)](https://bintray.com/hseeberger/sbt-plugins/sbt-header/_latestVersion)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Join the chat at https://gitter.im/sbt/sbt-header](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/sbt/sbt-header?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

sbt-header is an [sbt](http://www.scala-sbt.org) plugin for creating or updating file headers, e.g. copyright headers.

## Requirements

- Java 7 or higher
- sbt 0.13.9 or higher

## Configuration

In order to add the sbt-header plugin to your build, add the following line to `project/plugins.sbt`:

``` scala
addSbtPlugin("de.heikoseeberger" % "sbt-header" % "1.7.0")
```

If your build uses an auto plugin for common settings, make sure to add `HeaderPlugin` to `requires`:

``` scala
import de.heikoseeberger.sbtheader.HeaderPlugin

object Build extends AutoPlugin {
  override def requires = ... && HeaderPlugin
  ...
}
```

You have to define which source or resource files should be considered by sbt-header and if so, how the headers should look like. sbt-header uses a mapping from file extension to header pattern and text for that purpose, specified with the `headers` setting. Here's an example:

``` scala
import de.heikoseeberger.sbtheader.HeaderPattern

headers := Map(
  "scala" -> (
    HeaderPattern.cStyleBlockComment,
    """|/*
       | * Copyright 2015 Heiko Seeberger
       | *
       | * Licensed under the Apache License, Version 2.0 (the "License");
       | * you may not use this file except in compliance with the License.
       | * You may obtain a copy of the License at
       | *
       | *    http://www.apache.org/licenses/LICENSE-2.0
       | *
       | * Unless required by applicable law or agreed to in writing, software
       | * distributed under the License is distributed on an "AS IS" BASIS,
       | * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
       | * See the License for the specific language governing permissions and
       | * limitations under the License.
       | */
       |
       |""".stripMargin
  )
)
```

To exclude some files, use the `excludes` setting. It accepts glob file name patterns relative to the project root:

``` scala
import de.heikoseeberger.sbtheader.HeaderPattern

headers := Map(
  "scala" -> Apache2_0("2015", "Heiko Seeberger")
)

excludes := Seq(
  "src/generated/**",
)
```

The most common licenses have been pre-canned in [License](https://github.com/sbt/sbt-header/blob/master/src/main/scala/de/heikoseeberger/sbtheader/license/License.scala):
- [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)
- [MIT License](https://opensource.org/licenses/MIT)
- BSD [2 Clause](https://opensource.org/licenses/BSD-2-Clause) and [3 Clause](https://opensource.org/licenses/BSD-3-Clause) License
- [GNU General Public License v3](http://www.gnu.org/licenses/gpl-3.0.en.html)
- [GNU Lesser General Public License v3](http://www.gnu.org/licenses/lgpl-3.0.en.html)
- [GNU Affero General Public License v3](https://www.gnu.org/licenses/agpl.html)

They can be added as follows:

``` scala
import de.heikoseeberger.sbtheader.license.Apache2_0

headers := Map(
  "scala" -> Apache2_0("2015", "Heiko Seeberger"),
  "conf" -> Apache2_0("2015", "Heiko Seeberger", "#")
)
```

The built-in licenses support three comment styles:
- C style block comments (default)
- C++ style line comments (commentStyle = "//")
- Hash line comments (commentStyle = "#")

The CommentStyleMapping object in [HeaderPlugin](https://github.com/sbt/sbt-header/blob/master/src/main/scala/de/heikoseeberger/sbtheader/HeaderPlugin.scala) provides default mappings for common file types, so that they don't have to be configured manually:

``` scala
import de.heikoseeberger.sbtheader.license.Apache2_0
import de.heikoseeberger.sbtheader.CommentStyleMapping._

headers := createFrom(Apache2_0, "2015", "Heiko Seeberger")
```

Notice that for the header pattern you have to provide a `Regex` which extracts the header and the body for a given file, i.e. one with two capturing groups. `HeaderPattern` defines three widely used patterns:
- `cStyleBlockComment`, e.g. for Scala and Java
- `cppStyleLineComment`, e.g. for C++ and Protobuf
- `hashLineComment`, e.g. for Bash, Python and HOCON

By the way, first lines starting with shebang are not touched by sbt-header.

You can also declare your own quite easily using the `HeaderPattern.commentBetween` and `HeaderPattern.commentStartingWith` functions.

By default sbt-header takes `Compile` and `Test` configurations into account. If you need more, just add them:

``` scala
HeaderPlugin.settingsFor(It, MultiJvm)
```

## Usage

In order to create or update file headers, execute the `createHeaders` task:

```
> createHeaders
[info] Headers created for 2 files:
[info]   /Users/heiko/projects/sbt-header/sbt-header-test/test.scala
[info]   /Users/heiko/projects/sbt-header/sbt-header-test/test2.scala
```

In order to check whether all files have headers, execute the `checkHeaders` task:

```
> checkHeaders
[error] (compile:checkHeaders) There are files without headers!
[error]   /Users/heiko/projects/sbt-header/sbt-header-test/test.scala
[error]   /Users/heiko/projects/sbt-header/sbt-header-test/test2.scala
```

### Automation

If you want to automate header creation/update on compile, enable the `AutomateHeaderPlugin`:

``` scala
lazy val myProject = project
  .in(file("."))
  .enablePlugins(AutomateHeaderPlugin)
```

By default automation takes `Compile` and `Test` configurations into account. If you need more, just add them:

``` scala
AutomateHeaderPlugin.automateFor(It, MultiJvm)
```

## Integration with other plugins

This plugin by default only handles `managedSources` and `managedResources` in `Compile` and `Test`. For this reason you
need to tell sbt-header when it should also add headers to additional files managed by other plugins.

### sbt-boilerplate

In order to use sbt-header with [sbt-boilerplate plugin](https://github.com/sbt/sbt-boilerplate) add the following to
your build definition:

```scala
def addBoilerplate(confs: Configuration*) = confs.foldLeft(List.empty[Setting[_]]) { (acc, conf) =>
  acc ++ (unmanagedSources in (conf, createHeaders) := (((sourceDirectory in conf).value / "boilerplate") ** "*.template").get)
}

addBoilerplate(Compile, Test)
```

This adds `src/{conf}/boilerplate/**.scala` in the list of files handled by sbt-headers for `conf`, where `conf` is
either `Compile` or `Test`.

### sbt-twirl / play projects

To use sbt-header in a project using [sbt-twirl](https://github.com/playframework/twirl) (for example a Play web
project), the Twirl templates have to be added to the sources handled by sbt-header. Add the following to your build
definition:

```scala
import de.heikoseeberger.sbtheader.license.Apache2_0
import play.twirl.sbt.Import.TwirlKeys

headers := Map(
  "html" -> Apache2_0("2015", "Heiko Seeberger", "@*")
)

unmanagedSources.in(Compile, createHeaders) ++= sources.in(Compile, TwirlKeys.compileTemplates).value
```

sbt-header supports two comment styles for Twirl templates. `@*` will produce simple twirl comments, while `@**` produce
twirl block comments.

`@*` comment style:

```scala
@*
 * This is a simple twirl comment
 *@
```

`@**` comment style:

```scala
@*********************************
 * This is a twirl block comment *
 ********************************@
```

## Contribution policy ##

Contributions via GitHub pull requests are gladly accepted from their original author. Along with any pull requests, please state that the contribution is your original work and that you license the work to the project under the project's open source license. Whether or not you state this explicitly, by submitting any copyrighted material via pull request, email, or other means you agree to license the material under the project's open source license and warrant that you have the legal authority to do so.

## License ##

This code is open source software licensed under the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html).
