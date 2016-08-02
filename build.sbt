name := "feedback-streamer"

version := "1.0"

scalaVersion := "2.11.8"

resolvers += "spray repo" at "http://repo.spray.io"
resolvers += "softprops-maven" at "http://dl.bintray.com/content/softprops/maven"

scalacOptions := Seq(
  "-encoding", "utf8",
  "-feature",
  "-unchecked",
  "-deprecation",
  "-target:jvm-1.8",
  "-Ymacro-debug-lite",
  "-language:_",
  "-Xexperimental"
)
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-streaming-kafka" % "1.6.2",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.8",
  "org.apache.kafka" %% "kafka" % "0.8.2.1",
  "com.datastax.spark" %% "spark-cassandra-connector-embedded" % "1.6.0",
  "org.scala-lang" % "scala-reflect" % "2.11.8",
  "io.spray" %% "spray-can" % "1.3.3",
  "io.spray" %% "spray-routing" % "1.3.3",
  "io.spray" %% "spray-testkit" % "1.3.3" % "test",
  "com.typesafe.akka" %% "akka-actor" % "2.3.9",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.9" % "test",
  "me.lessis"          %% "courier"          % "0.1.3",
  "javax.mail" % "mail" % "1.5.0-b01",
  "com.typesafe" % "config" % "1.2.1"
)
