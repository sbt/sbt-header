import sbt._

object Version {
  //val scala     = "2.11.5"
  val scalaTest = "2.2.3"
}

object Library {
  val scalaTest = "org.scalatest" %% "scalatest" % Version.scalaTest
}
