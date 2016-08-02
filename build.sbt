name := "i-m-a-good"

version := "1.0"

scalaVersion := "2.11.8"

resolvers += "spray repo" at "http://repo.spray.io"
resolvers += "softprops-maven" at "http://dl.bintray.com/content/softprops/maven"

libraryDependencies ++= {
  val akkaV = "2.3.9"
  val sprayV = "1.3.3"
  Seq(
    "io.spray" %% "spray-can" % sprayV,
    "io.spray" %% "spray-routing" % sprayV,
    "io.spray" %% "spray-testkit" % sprayV % "test",
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-testkit" % akkaV % "test",
    "me.lessis"          %% "courier"          % "0.1.3",
    "javax.mail" % "mail" % "1.5.0-b01",
    "com.typesafe" % "config" % "1.2.1"
  )
}
