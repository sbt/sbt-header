/*
 * Copyright 2015 Heiko Seeberger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.heikoseeberger.sbtheader
package license

import scala.util.matching.Regex

trait License {
  def apply(yyyy: String, copyrightOwner: String): (Regex, String)
}

object Apache2_0 extends License {
  override def apply(yyyy: String, copyrightOwner: String) = {
    val text = s"""|/*
                   | * Copyright $yyyy $copyrightOwner
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
    (SbtHeader.HeaderPattern.cStyleBlockComment, text)
  }
}
