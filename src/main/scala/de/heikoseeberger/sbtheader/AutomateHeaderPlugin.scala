/*
 * Copyright 2015 Heiko Seeberger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.heikoseeberger.sbtheader

import sbt.{ AutoPlugin, Compile, Configuration, Setting, Test, inConfig }
import sbt.Keys.compile

/**
  * Enable this plugin to automate header creation/update on compile. By default the `Compile` and
  * `Test` configurations are considered; use
  * [[AutomateHeaderPlugin.autoImport.automateHeaderSettings]] to add further ones.
  */
object AutomateHeaderPlugin extends AutoPlugin {

  final object autoImport {

    def automateHeaderSettings(configurations: Configuration*): Seq[Setting[_]] =
      configurations.foldLeft(List.empty[Setting[_]]) {
        _ ++ inConfig(_)(compile := compile.dependsOn(HeaderPlugin.autoImport.headerCreate).value)
      }
  }

  override def requires = HeaderPlugin

  override def projectSettings = autoImport.automateHeaderSettings(Compile, Test)
}
