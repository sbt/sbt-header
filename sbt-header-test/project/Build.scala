import de.heikoseeberger.sbtheader.HeaderPlugin
import de.heikoseeberger.sbtheader.license.Apache2_0
import sbt._

object Build extends AutoPlugin {

  override def requires = plugins.JvmPlugin && HeaderPlugin

  override def trigger = allRequirements

  override def projectSettings = List(
    HeaderPlugin.autoImport.headers := Map("scala" -> Apache2_0("2015", "Heiko Seeberger"))
  )
}
