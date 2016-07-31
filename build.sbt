name := "i-m-a-good"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-streaming-kafka" % "1.6.2",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.8",
  "org.apache.kafka" %% "kafka" % "0.10.0.0"
)
