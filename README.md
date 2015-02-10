# sbt-header #

sbt-header is an [sbt](http://www.scala-sbt.org) plugin for creating or updating file headers, e.g. copyright headers.

## Usage

In order to add the sbt-header plugin to your build, just add the below line to `project/plugins.sbt`:

``` scala
addSbtPlugin("de.heikoseeberger" % "sbt-header" % "0.5.1")
```

You have to define which source files should have which header: sbt-header uses a mapping from file extension to
header pattern and text for that purpose, specified with the `headers` setting. Add a line like the one below
to your `build.sbt`:

``` scala
headers := Map(
  "scala" -> (
    HeaderPattern.blockComment,
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

In order to create or update the file headers, execute the `createHeaders` task:

```
> createHeaders
[info] Headers created for 2 files:
[info]   /Users/heiko/projects/sbt-header/sbt-header-test/test.scala
[info]   /Users/heiko/projects/sbt-header/sbt-header-test/test2.scala
```

If you want to automate header creation/update for `Config` and `Test` configurations,
add the following settings to your build:

``` scala
inConfig(Compile)(compileInputs.in(compile) <<= compileInputs.in(compile).dependsOn(createHeaders.in(compile)))
inConfig(Test)(compileInputs.in(compile) <<= compileInputs.in(compile).dependsOn(createHeaders.in(compile)))
```

## Contribution policy ##

Contributions via GitHub pull requests are gladly accepted from their original author. Along with any pull requests, please state that the contribution is your original work and that you license the work to the project under the project's open source license. Whether or not you state this explicitly, by submitting any copyrighted material via pull request, email, or other means you agree to license the material under the project's open source license and warrant that you have the legal authority to do so.

## License ##

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
