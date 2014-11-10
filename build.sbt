organization := "com.explora"

name := "explora-core"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.4"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io/"
)

libraryDependencies ++= {
  val sprayV = "1.3.1"
  val akkaV = "2.3.6"
  Seq(
    "io.spray" % "spray-can" % sprayV,
    "io.spray" % "spray-client" % sprayV,
    "com.typesafe.akka" %% "akka-actor" % akkaV
  )
}

