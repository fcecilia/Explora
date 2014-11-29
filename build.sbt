organization := "com.explora"

name := "explora-core"

version := "0.1.1-SNAPSHOT"

scalaVersion := "2.10.4"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers ++= Seq(
    "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/releases/"
)

libraryDependencies ++= {
  val sesameV: String = "2.7.14"
  Seq( "org.openrdf.sesame" % "sesame" %  sesameV,
    "org.openrdf.sesame" % "sesame-repository-http" %  sesameV,
    "org.openrdf.sesame" % "sesame-queryresultio-sparqlxml" % sesameV,
    "commons-logging" % "commons-logging" % "1.2"
  )
}

