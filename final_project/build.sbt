name := "web-service"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.13",
  "com.typesafe.akka" %% "akka-stream" % "2.5.13",
  "com.typesafe.akka" %% "akka-http" % "10.1.3",
  "org.apache.pdfbox" % "pdfbox" % "2.0.1",
  "org.apache.pdfbox" % "fontbox" % "2.0.1",
  "com.typesafe" % "config" % "1.3.0",
)

