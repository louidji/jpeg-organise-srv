import _root_.sbt.Keys._
import com.typesafe.sbt.packager.docker._

name := """upload-img-srv"""

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



dockerRepository := Some("louidji")
dockerExposedPorts in Docker := Seq(9000)


//dockerCommands ++= Seq(
//  ExecCmd("RUN", "mkdir", "-p", "/tmp/upload"),
//  ExecCmd("RUN", "chown", "-R", "daemon:daemon", "/tmp/upload")
//)
//maintainer := "louidji"
//dockerExposedVolumes += "/data/images"





dockerCommands := Seq(
	Cmd("FROM", "java:latest"),	
	Cmd("MAINTAINER", "louidji"),
	Cmd("WORKDIR", "/opt/docker"),
	Cmd("COPY", "opt /opt"),
	ExecCmd("RUN", "chown", "-R", "daemon:daemon", "."),
	ExecCmd("RUN", "mkdir", "-p", "/data/images"),
	ExecCmd("RUN", "chown", "-R", "daemon:daemon", "/data/images"),
	ExecCmd("RUN", "mkdir", "-p", "/tmp/upload"),
	ExecCmd("RUN", "chown", "-R", "daemon:daemon", "/tmp/upload"),
	ExecCmd("VOLUME", "/data/images"),
	Cmd("USER", "daemon"),
	ExecCmd("ENTRYPOINT", "bin/" + name.value),
	ExecCmd("CMD")
)

/*
FROM java:latest
MAINTAINER louidji
WORKDIR /opt/docker
ADD opt /opt
RUN ["chown", "-R", "daemon:daemon", "."]
RUN ["mkdir", "-p", "/data/images"]
RUN ["chown", "-R", "daemon:daemon", "/data/images"]
RUN ["mkdir", "-p", "/tmp/upload"]
RUN ["chown", "-R", "daemon:daemon", "/tmp/upload"]
VOLUME ["/data/images"]
USER daemon
ENTRYPOINT ["bin/upload-img-srv"]
CMD []
*/



