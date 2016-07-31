name := "feedback-streamer"

version := "1.0"

scalaVersion := "2.11.8"

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
  "org.scala-lang" % "scala-reflect" % "2.11.8"
)
