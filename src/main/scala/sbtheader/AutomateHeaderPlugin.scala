/*
 * Copyright (c) 2015 - 2025, Heiko Seeberger
 * Copyright (c) 2025, sbt plugin contributors
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package sbtheader

import sbt.{ *, given }
import sbt.Keys.compile
import PluginCompat.{ *, given }

/**
  * Enable this plugin to automate header creation/update on compile. By default the `Compile` and
  * `Test` configurations are considered; use
  * [[AutomateHeaderPlugin.autoImport.automateHeaderSettings]] to add further ones.
  */
object AutomateHeaderPlugin extends AutoPlugin {

  object autoImport {

    def automateHeaderSettings(configurations: Configuration*): Seq[Setting[?]] =
      configurations.foldLeft(List.empty[Setting[?]]) {
        _ ++ inConfig(_)(
          compile := Def.uncached {
            compile.dependsOn(HeaderPlugin.autoImport.headerCreate).value
          }
        )
      }
  }

  override def requires: Plugins =
    HeaderPlugin

  override def projectSettings: Seq[Def.Setting[?]] =
    autoImport.automateHeaderSettings(Compile, Test)
}
