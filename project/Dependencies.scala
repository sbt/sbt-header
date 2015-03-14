import sbt._

object Version {
  //val scala     = "2.11.6"
  val scalaTest = "2.2.4"
}

object Library {
  val scalaTest = "org.scalatest" %% "scalatest" % Version.scalaTest
}
