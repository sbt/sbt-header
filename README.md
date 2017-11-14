# sbt-header #

[![Build Status](https://travis-ci.org/sbt/sbt-header.svg?branch=master)](https://travis-ci.org/sbt/sbt-header)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/2cd1ffe963484f45902e8ca2a72276c6)](https://www.codacy.com/app/britter/sbt-header?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=sbt/sbt-header&amp;utm_campaign=Badge_Grade)
[![Download](https://api.bintray.com/packages/hseeberger/sbt-plugins/sbt-header/images/download.svg)](https://bintray.com/hseeberger/sbt-plugins/sbt-header/_latestVersion)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Join the chat at https://gitter.im/sbt/sbt-header](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/sbt/sbt-header?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

sbt-header is an [sbt](http://www.scala-sbt.org) plugin for creating or updating file headers, e.g. copyright headers.

## Getting started

In order to add the sbt-header plugin to your build, add the following line to `project/plugins.sbt`:

``` scala
addSbtPlugin("de.heikoseeberger" % "sbt-header" % "3.0.2")
```

Then in your `build.sbt` configure the following settings:

```scala
organizationName := "Heiko Seeberger"
startYear := Some(2015)
licenses += ("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt"))
```

This configuration will apply Apache License 2.0 headers to Scala and Java files. sbt-header provides two tasks: `headerCreate` and `headerCheck`, which are described in the following sub sections. For more information on how to customize sbt-header, please refer to the [Configuration](#configuration) section.

### Creating headers

In order to create or update file headers, execute the `headerCreate` task:

```
> headerCreate
[info] Headers created for 2 files:
[info]   /Users/heiko/projects/sbt-header/sbt-header-test/test.scala
[info]   /Users/heiko/projects/sbt-header/sbt-header-test/test2.scala
```

### Checking headers

In order to check whether all files have headers (for example for CI), execute the `headerCheck` task:

```
> headerCheck
[error] (compile:checkHeaders) There are files without headers!
[error]   /Users/heiko/projects/sbt-header/sbt-header-test/test.scala
[error]   /Users/heiko/projects/sbt-header/sbt-header-test/test2.scala
```

`headerCheck` will not modify any files but will cause the build to fail if there are files without a license header.

### Requirements

- Java 8 or higher
- sbt 1.0.0 or higher

## Configuration

By default sbt-header tries to infer the license header you want to use from the `orgaizationName`, `startYear` and `licenses` settings. For this to work, sbt-header requires the `licenses` setting to contain exactly one entry. The first component of that entry has to be the [SPDX license identifier](http://spdx.org/licenses/) of one of the [supported licenses](#build-in-licenses).

### Setting the license to use explicitly

If you can not setup your build in a way that sbt-header can detect the license you want to use (see above), you can set the license to use explicitly:

```scala
headerLicense := Some(HeaderLicense.MIT("2015", "Heiko Seeberger"))
```

This will also be given precedence if a license has been auto detected from project settings.

### Build in licenses

The most common licenses have been pre-canned in [License](https://github.com/sbt/sbt-header/blob/master/src/main/scala/de/heikoseeberger/sbtheader/License.scala). They can either be detected using their SPDX identifier or by setting them explicitly.

|License|SPDX identifier|
|-------|---------------|
|[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)|`Apache-2.0`|
|[BSD 2 Clause](https://opensource.org/licenses/BSD-2-Clause)|`BSD-2-Clause`|
|[BSD 3 Clause](https://opensource.org/licenses/BSD-3-Clause)|`BSD-3-Clause`|
|[GNU General Public License v3](http://www.gnu.org/licenses/gpl-3.0.en.html)|`GPL-3.0`|
|[GNU Lesser General Public License v3](http://www.gnu.org/licenses/lgpl-3.0.en.html)|`LGPL-3.0`|
|[GNU Affero General Public License v3](https://www.gnu.org/licenses/agpl.html)|`AGPL-3.0`|
|[MIT License](https://opensource.org/licenses/MIT)|`MIT`|
|[Mozilla Public License, v. 2.0](http://mozilla.org/MPL/2.0/)|`MPL-2.0`|

### Using a custom license text

If you don't want to use one of the build in licenses, you can define a custom license text using the `Custom` case class:

```scala
headerLicense := Some(HeaderLicense.Custom(
  """|Copyright (c) Awesome Company 2015
     |
     |This is the custom License of Awesome Company
     |""".stripMargin
))
```

Note that you don't need to add comment markers like `//` or `/*`. The comment style is configured on a per file type basis (see [next section](#configuring-comment-styles)).

### Configuring comment styles

Comment styles are configured on a per file type basis. The default is to apply C Style block comments to Scala and Java files. No other comment styles are predefined. If you want to create comments for example for your XML files, you have to add the corresponding mapping manually (see below). The build-in comment styles are defined in [CommentStyle](https://github.com/sbt/sbt-header/blob/master/src/main/scala/de/heikoseeberger/sbtheader/CommentStyle.scala):

|Name|Description|
|----|-----------|
|cStyleBlockComment|C style block comments (blocks starting with "/\*" and ending with "\*/")|
|cppStyleLineComment|C++ style line comments (lines prefixed with "//")|
|hashLineComment|Hash line comments (lines prefixed with "#")|
|twirlStyleComment|Twirl style comment (blocks starting with "@\*" and ending with "\*@")|
|twirlStyleBlockComment|Twirl style block comments (comment blocks with a frame made of "*")|

To override the configuration for Scala/Java files or add a configuration for some other file type, use the `headerMapping` setting:

``` scala
headerMappings := headerMappings.value + (HeaderFileType.scala -> HeaderCommentStyle.cppStyleLineComment)
```

### Excluding files

To exclude some files, use the [sbt's file filters](http://www.scala-sbt.org/0.13/docs/Howto-Customizing-Paths.html#Include%2Fexclude+files+in+the+source+directory):

``` scala
excludeFilter.in(unmanagedSources.in(headerCreate)) := HiddenFileFilter || "*Excluded.scala"
```

### Using an auto plugin

If your build uses an auto plugin for common settings, make sure to add `HeaderPlugin` to `requires`:

``` scala
import de.heikoseeberger.sbtheader.HeaderPlugin

object Build extends AutoPlugin {
  override def requires = ... && HeaderPlugin
  ...
}
```

### Adding headers to files in other configurations

By default sbt-header takes `Compile` and `Test` configurations into account. If you need more, just add them:

``` scala
HeaderPlugin.settingsFor(It, MultiJvm)
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
need to tell sbt-header if it should also add headers to additional files managed by other plugins.

### sbt-twirl / play projects

To use sbt-header in a project using [sbt-twirl](https://github.com/playframework/twirl) (for example a Play web
project), the Twirl templates have to be added to the sources handled by sbt-header. Add the following to your build
definition:

```scala
import de.heikoseeberger.sbtheader.FileType
import play.twirl.sbt.Import.TwirlKeys

headerMappings := headerMappings.value + (FileType("html") -> HeaderCommentStyle.twirlStyleBlockComment)

unmanagedSources.in(Compile, headerCreate) ++= sources.in(Compile, TwirlKeys.compileTemplates).value
```

sbt-header supports two comment styles for Twirl templates. `twirlStyleBlockComment` will produce simple twirl block comments, while `twirlStyleFramedBlockComment` will produce
framed twirl comments.

`twirlStyleBlockComment` comment style:

```scala
@*
 * This is a simple twirl block comment
 *@
```

`twirlStyleFramedBlockComment` comment style:

```scala
@**********************************
 * This is a framed twirl comment *
 **********************************@
```

### sbt-boilerplate

In order to use sbt-header with [sbt-boilerplate plugin](https://github.com/sbt/sbt-boilerplate) add the following to
your build definition:

```scala
def addBoilerplate(confs: Configuration*) = confs.foldLeft(List.empty[Setting[_]]) { (acc, conf) =>
  acc ++ (unmanagedSources in (conf, headerCreate) := (((sourceDirectory in conf).value / "boilerplate") ** "*.template").get)
}

addBoilerplate(Compile, Test)
```

This adds `src/{conf}/boilerplate/**.scala` in the list of files handled by sbt-headers for `conf`, where `conf` is
either `Compile` or `Test`.

## Migrating from 1.x

This section contains migration notes from version 1.x of sbt-header to version 2.x. The latest release of the 1.x line is 1.8.0. You can find the documentation of that release in the [corresponding git tag](https://github.com/sbt/sbt-header/tree/v1.8.0).

### Changed task names and settings keys

The names of all tasks and settings have been changed from 1.x to 2.x. Furthermore types of settings have changed. The following tables give an overview of the changes:

Changed task names:

|Old Name|New Name|
|--------|--------|
|`createHeaders`|`headerCreate`|
|`checkHeaders`|`headerCheck`|

Changed settings:

|Old Name : Old Type|New Name: New Type|
|--------|--------|
|`headers : Map[String, (Regex, String)]`|`headerMappings : Map[FileType, CommentStyle]`|
| - | `headerLicense : Option[License]`|
| `exclude : Seq[String]` | removed in favor of sbt include/excude filters|

### `createFrom` method

sbt-header 1.x featured some default header mappings as well as the `createFrom` method, which could be used to easily define header mappings:

```scala
headers := createFrom(Apache2_0, "2015", "Heiko Seeberger")
```

This method has been removed and the default mappings for Scala and Java files has been added as default mapping to the `headerMappings` setting.

### Custom licenses

In sbt-header 1.x when you needed to use a custom license this would typically look like this:

```scala
headers := Map(
  "scala" -> (
    HeaderPattern.cStyleBlockComment,
    """|/*
       | * Copyright 2015 Awesome Company
       | */
       |""".stripMargin
  )
)
```

In sbt-header 2.x, licenses are defined as instances of `de.hseeberger.sbtheader.License`. Further more, the license is only defined once and not per file type. So the above in 2.x is equivalent to:

```scala
headerLicense := Some(HeaderLicense.Custom(
    """|Copyright 2015 Awesome Company
       |""".stripMargin
))
```

Note that you only need to define the license text, but not the comment markers. The latter are configured via the `headerMappings` setting. The configuration above will use the default mappings which apply C style block comments to Java and Scala files. If you have mappings for additional file types, please add these to the `headerMappings` setting.

### Dropped features

In sbt-header 1.x it was possible to define different licenses for different files types, e.g.:

```scala
headers := Map(
  "scala" -> Apache2_0("2015", "Heiko Seeberger"),
  "java" -> MIT("2015", "Heiko Seeberger")
)
```

Since we believe most of the projects out there will only ever have one license, we dropped this feature without replacement. In sbt-header 2.x users have to define a single license for the whole project using the `headerLicense` setting (or let sbt-header infer it from the `licenses` project setting, see [above](#getting-started)) and a mapping from file type to comment style using the `headerMappings` setting.

## Contribution policy ##

Contributions via GitHub pull requests are gladly accepted from their original author. Along with any pull requests, please state that the contribution is your original work and that you license the work to the project under the project's open source license. Whether or not you state this explicitly, by submitting any copyrighted material via pull request, email, or other means you agree to license the material under the project's open source license and warrant that you have the legal authority to do so.

## License ##

This code is open source software licensed under the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html).
