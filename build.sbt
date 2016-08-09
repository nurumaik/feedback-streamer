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
  "org.apache.kafka" %% "kafka" % "0.8.2.1",
  "com.datastax.spark" %% "spark-cassandra-connector-embedded" % "1.6.0",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.8",
  "com.typesafe.akka" %% "akka-actor" % "2.4.8",
  "com.typesafe.akka" %% "akka-testkit" % "2.4.8",
  "com.typesafe.akka" %% "akka-stream" % "2.4.8",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.8",
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % "2.4.8",
  "com.typesafe.akka" %% "akka-http-testkit" % "2.4.8" % "test",
  "me.lessis"          %% "courier"          % "0.1.3",
  "com.typesafe" % "config" % "1.3.0",
  "org.scalatest" % "scalatest_2.11" % "3.0.0" % "test",
  "org.scala-lang" % "scala-reflect" % "2.11.8",
  "org.jvnet.mock-javamail" % "mock-javamail" % "1.9" % "test"
)
