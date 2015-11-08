import _root_.sbt.Keys._

name := """play-scala"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

unmanagedBase <<= baseDirectory { base => base / "custom_lib" }

libraryDependencies ++= {
  Seq(
    //jdbc,
    cache,
    ws,
    "com.drewnoakes" % "metadata-extractor" % "2.8.1",
    "com.googlecode.mp4parser" % "isoparser" % "1.1.7",
    specs2 % Test
  )
}
resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator


fork in run := true
