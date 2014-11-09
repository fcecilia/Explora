organization := "com.explora"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.2"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io/"
)

libraryDependencies ++= {
  val sprayV = "1.3.2"
  Seq(
    "io.spray" % "spray-can" % sprayV,
    "io.spray" % "spray-client" % sprayV
  )
}

